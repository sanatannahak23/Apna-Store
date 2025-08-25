package com.apna_store.order_service.controller;

import com.apna_store.order_service.dto.request.OrderRequest;
import com.apna_store.order_service.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    //    POST /orders → create order
    public ResponseEntity<ApiResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        return null;
    }

    //    GET /orders/{orderId} → fetch order details
    public ResponseEntity<ApiResponse> getByOrderId(@PathVariable("orderId") Long orderId) {
        return null;
    }

    //    GET /orders/user/{userId} → order history
    public ResponseEntity<ApiResponse> getByUserId(@PathVariable("orderId") Long orderId) {
        return null;
    }

    //    PUT /orders/{orderId}/status → update status (payment callback/admin)
    public ResponseEntity<ApiResponse> updateStatus(@PathVariable("orderId") Long orderId) {
        return null;
    }

    //    PUT /orders/{orderId}/cancel → cancel order
    public ResponseEntity<ApiResponse> cancelOrder(@PathVariable("orderId") Long orderId) {
        return null;
    }

    //    GET /orders → list all orders (admin)
    public ResponseEntity<ApiResponse> getAllOrders() {
        return null;
    }

    //    PUT /orders/{orderId} → update order (admin)
//    public ResponseEntity<ApiResponse> updateOrder(@PathVariable("orderId") Long orderId) {
//        return null;
//    }
}
