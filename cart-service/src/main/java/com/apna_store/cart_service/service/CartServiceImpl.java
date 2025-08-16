package com.apna_store.cart_service.service;

import com.apna_store.cart_service.client.UserClient;
import com.apna_store.cart_service.dto.response.ApiResponse;
import com.apna_store.cart_service.client.ProductClient;
import com.apna_store.cart_service.dto.request.AddToCartRequest;
import com.apna_store.cart_service.dto.response.CartResponse;
import com.apna_store.cart_service.dto.response.ProductResponse;
import com.apna_store.cart_service.entities.Cart;
import com.apna_store.cart_service.entities.CartItem;
import com.apna_store.cart_service.exception.DataAlreadyExistException;
import com.apna_store.cart_service.exception.DataNotFoundException;
import com.apna_store.cart_service.exception.ProductNotFound;
import com.apna_store.cart_service.helper.EntityToResponse;
import com.apna_store.cart_service.messages.ExceptionMessages;
import com.apna_store.cart_service.repository.CartItemRepository;
import com.apna_store.cart_service.repository.CartRepository;
import com.apna_store.cart_service.service.services.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    private final ProductClient productClient;

    private final UserClient userClient;

    private final ObjectMapper objectMapper;

    @Override
    public CartResponse createCart(Long id) {
        cartRepository.findById(id)
                .ifPresent(ex -> {
                    throw new DataAlreadyExistException(ExceptionMessages.ALREADY_CART_EXIST);
                });

        // It's need to verify the id with user service
        ApiResponse response;
        try {
            response = userClient.getUserById(id);
        } catch (Exception ex) {
            throw new DataNotFoundException(ex.getMessage());
        }
        if (response == null || response.getData() == null)
            throw new DataNotFoundException(ExceptionMessages.USER_NOT_FOUND);

        log.info("User Details :: {}", response.getData());
        Cart cart = new Cart();
        cart.setCartId(id);
        cart = cartRepository.save(cart);
        return EntityToResponse.cartToCartResponse(cart);
    }

    @Override
    public CartResponse getCartById(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.INVALID_CART_ID));
        return EntityToResponse.cartToCartResponse(cart);
    }

    @Override
    public CartResponse addToCart(Long id, AddToCartRequest addToCartRequest) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.INVALID_CART_ID));

        // Need to get the product details from product service by feign client
        ApiResponse response;
        try {
            response = productClient.getProductById(addToCartRequest.getProductId());
        } catch (Exception exception) {
            throw new ProductNotFound(exception.getMessage());
        }
        if (response == null || response.getData() == null)
            throw new DataNotFoundException(ExceptionMessages.PRODUCT_NOT_FOUND);

        log.info("Product Data :: {}", response.getData());
        ProductResponse productResponse = objectMapper.convertValue(response.getData(), ProductResponse.class);

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setPrice(productResponse.getPrice().toBigInteger().doubleValue());
        cartItem.setProductId(addToCartRequest.getProductId());
        cartItem.setQuantity(addToCartRequest.getQuantity());
        cartItem.setTotalPrice(productResponse.getPrice().multiply(BigDecimal.valueOf(addToCartRequest.getQuantity())));
        cartItem.setProductName(productResponse.getName());

        cart.getItems().add(cartItem);
        cart.setTotalPrice(cart.getTotalPrice() != null
                ? cart.getTotalPrice().add(cartItem.getTotalPrice())
                : cartItem.getTotalPrice());
        cart = cartRepository.save(cart);
        return EntityToResponse.cartToCartResponse(cart);
    }

    @Override
    public CartResponse updateItemQuantity(Long id, Long itemId, Integer quantity) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.INVALID_CART_ID));

        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.INVALID_CART_ITEM_ID));

        List<CartItem> items = cart.getItems();
        if (items == null || items.isEmpty())
            throw new DataNotFoundException(ExceptionMessages.EMPTY_CART);

        boolean check = false;
        for (CartItem item : items) {
            if (item.getId().equals(cartItem.getId())) {
                cart.setTotalPrice(cart.getTotalPrice().subtract(item.getTotalPrice()));
                item.setQuantity(quantity);
                item.setTotalPrice(BigDecimal.valueOf(item.getPrice() * quantity));
                cart.setTotalPrice(cart.getTotalPrice().add(item.getTotalPrice()));
                check = true;
                break;
            }
        }
        if (!check) throw new DataNotFoundException(ExceptionMessages.ITEM_NOT_AVAILABLE_IN_CART);
        cart.setItems(items);
        cart = cartRepository.save(cart);
        return EntityToResponse.cartToCartResponse(cart);
    }

    @Override
    public CartResponse removeItem(Long id, Long itemId) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.INVALID_CART_ID));

        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.INVALID_CART_ITEM_ID));

        List<CartItem> items = cart.getItems();
        if (items == null || items.isEmpty())
            throw new DataNotFoundException(ExceptionMessages.EMPTY_CART);

        boolean check = false;
        Iterator<CartItem> iterator = items.iterator();
        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            if (item.getId().equals(cartItem.getId())) {
                cartItemRepository.delete(item);
                cart.setTotalPrice(cart.getTotalPrice().subtract(item.getTotalPrice()));
                iterator.remove();
                check = true;
                break;
            }
        }

        if (!check)
            throw new DataNotFoundException(ExceptionMessages.ITEM_NOT_AVAILABLE_IN_CART);
        cart.setItems(items);
        cart = cartRepository.save(cart);
        return EntityToResponse.cartToCartResponse(cart);
    }

    @Override
    public void clearCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.INVALID_CART_ID));

        List<CartItem> items = cart.getItems();
        if (items == null || items.isEmpty())
            throw new DataNotFoundException(ExceptionMessages.EMPTY_CART);
        cartItemRepository.deleteAll(items);
        items.clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cart.setItems(items);
        cartRepository.save(cart);
    }

    @Override
    public void deleteCart(Long userId) {
        log.info("Delete cart method get invoked :: {}", userId);
        Cart cart = cartRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.INVALID_CART_ID));
        cartRepository.delete(cart);
    }
}
