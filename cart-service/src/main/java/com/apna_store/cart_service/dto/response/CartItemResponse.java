package com.apna_store.cart_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {
    private Long itemId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double price;
    private BigDecimal totalPrice;
}
