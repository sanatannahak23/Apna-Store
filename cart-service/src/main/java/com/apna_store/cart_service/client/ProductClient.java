package com.apna_store.cart_service.client;

import com.apna_store.cart_service.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PRODUCT-CATALOG-SERVICE",
        path = "/api/products")
public interface ProductClient {

    @GetMapping("/{id}")
    ApiResponse getProductById(@PathVariable("id") Long id);

}
