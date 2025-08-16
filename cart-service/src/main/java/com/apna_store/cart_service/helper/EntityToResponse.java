package com.apna_store.cart_service.helper;

import com.apna_store.cart_service.dto.response.CartItemResponse;
import com.apna_store.cart_service.dto.response.CartResponse;
import com.apna_store.cart_service.entities.Cart;
import com.apna_store.cart_service.entities.CartItem;

public class EntityToResponse {

    public static CartResponse cartToCartResponse(Cart cart) {
        CartResponse cartResponse = new CartResponse();
        cartResponse.setCartId(cart.getCartId());
        cartResponse.setCartRef(cart.getCartRef());
        cartResponse.setTotalPrice(cart.getTotalPrice());
        cartResponse.setItems(cart.getItems() != null
                ? cart.getItems()
                .stream()
                .map(EntityToResponse::cartItemToCartItemResponse)
                .toList()
                : null);
        return cartResponse;
    }

    public static CartItemResponse cartItemToCartItemResponse(CartItem items) {
        CartItemResponse cartItemResponse = new CartItemResponse();
        cartItemResponse.setItemId(items.getId());
        cartItemResponse.setPrice(items.getPrice());
        cartItemResponse.setTotalPrice(items.getTotalPrice());
        cartItemResponse.setQuantity(items.getQuantity());
        cartItemResponse.setProductId(items.getProductId());
        cartItemResponse.setProductName(items.getProductName());
        return cartItemResponse;
    }
}
