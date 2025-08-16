package com.apnaStore.inventory_service.clients;

import com.apnaStore.inventory_service.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PRODUCT-CATALOG-SERVICE",
        path = "/api/products",
        configuration = com.apnaStore.inventory_service.config.FeignConfig.class)
public interface ProductClient {

    @GetMapping("/{id}")
    ApiResponse getProductById(@PathVariable("id") Long id);

}
