package com.apna_store.cart_service.client;

import com.apna_store.cart_service.dto.response.ApiResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICE",
        path = "/api/users")
public interface UserClient {

    @GetMapping("/{userId}")
    @CircuitBreaker(name = "userClient", fallbackMethod = "getUserByIdFallback")
    ApiResponse getUserById(@PathVariable("userId") Long userId);

    default ApiResponse getUserByIdFallback(Long userId, Throwable t) {
        return new ApiResponse(true, null, "Fallback: " + t.getMessage());
    }
}
