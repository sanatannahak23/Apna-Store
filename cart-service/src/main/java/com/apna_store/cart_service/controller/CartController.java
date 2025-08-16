package com.apna_store.cart_service.controller;

import com.apna_store.cart_service.dto.request.AddToCartRequest;
import com.apna_store.cart_service.dto.response.ApiResponse;
import com.apna_store.cart_service.messages.SuccessMessages;
import com.apna_store.cart_service.service.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // create cart
    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse> createCart(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse(Boolean.FALSE,
                        cartService.createCart(id),
                        SuccessMessages.CART_CREATED_SUCCESSFULLY));
    }

    // 1. Get cart by user ID
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getCartByUserId(@PathVariable("userId") Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(Boolean.FALSE,
                        cartService.getCartById(userId),
                        SuccessMessages.DATA_FETCHED_SUCCESSFULLY));
    }

    // 2. Add product to cart
    @PostMapping("/{userId}/add")
    public ResponseEntity<ApiResponse> addToCart(@PathVariable("userId") Long userId, @RequestBody AddToCartRequest addToCartRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(Boolean.FALSE,
                        cartService.addToCart(userId, addToCartRequest),
                        SuccessMessages.PRODUCT_ADDED_INTO_THE_CART));
    }

    // 3. Update item quantity
    @PutMapping("/{userId}/update/{itemId}/{quantity}")
    public ResponseEntity<ApiResponse> updateQuantity(@PathVariable("userId") Long userId,
                                                      @PathVariable("itemId") Long itemId,
                                                      @PathVariable("quantity") Integer quantity) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(Boolean.FALSE,
                        cartService.updateItemQuantity(userId, itemId, quantity),
                        SuccessMessages.DATA_UPDATED_SUCCESSFULLY));
    }

    // 4. Remove an item from cart
    @DeleteMapping("/{userId}/remove/{itemId}")
    public ResponseEntity<ApiResponse> removeItem(@PathVariable("userId") Long userId,
                                                  @PathVariable("itemId") Long itemId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(Boolean.FALSE,
                        cartService.removeItem(userId, itemId),
                        SuccessMessages.ITEM_REMOVED_FROM_CART));
    }

    // 5. Clear cart
    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable("userId") Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(Boolean.FALSE,
                        null,
                        SuccessMessages.CLEAR_CART));
    }

    // 6. Delete cart
    public ResponseEntity<ApiResponse> deleteCart(@PathVariable("userId") Long userId) {
        cartService.deleteCart(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(Boolean.FALSE,
                        null,
                        SuccessMessages.CART_REMOVED_SUCCESSFULLY));
    }
}
