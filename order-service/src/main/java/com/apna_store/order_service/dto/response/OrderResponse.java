package com.apna_store.order_service.dto.response;

import com.apna_store.order_service.entities.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Long orderId;

    private OrderStatus status;

    private BigDecimal totalAmount;

    private List<OrderItemResponse> orderItems;
}
