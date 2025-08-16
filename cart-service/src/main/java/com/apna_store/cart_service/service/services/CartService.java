package com.apna_store.cart_service.service.services;


import com.apna_store.cart_service.dto.request.AddToCartRequest;
import com.apna_store.cart_service.dto.response.CartResponse;

public interface CartService {

    CartResponse createCart(Long id);

    CartResponse getCartById(Long id);

    CartResponse addToCart(Long id, AddToCartRequest addToCartRequest);

    CartResponse updateItemQuantity(Long id, Long itemId, Integer quantity);

    CartResponse removeItem(Long id, Long itemId);

    void clearCart(Long id);

    void deleteCart(Long userId);
}
