package com.apna_store.order_service.service.services;

import com.apna_store.order_service.dto.request.OrderRequest;
import com.apna_store.order_service.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(OrderRequest orderRequest);

    OrderResponse getOrderById(Long orderId);

    List<OrderResponse> getOrderByUserId(Long userId);

    OrderResponse updateStatus(Long orderId);

    OrderResponse cancelOrder(Long orderId);

    List<OrderResponse> getAllOrders();

    OrderResponse updateOrder(Long orderId);
}
