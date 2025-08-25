package com.apna_store.order_service.clients;

import com.apna_store.order_service.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CART-SERVICE",
        path = "/api/cart")
public interface CartClient {

    @GetMapping("/{userId}")
    ApiResponse getCartByUserId(@PathVariable("userId") Long userId);
}
