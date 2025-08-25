package com.apna_store.order_service.clients;

import com.apna_store.order_service.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICE",
        path = "/api/users")
public interface UserClient {

    @GetMapping("/address/{addressId}/get-address")
    ApiResponse getAddressById(@PathVariable Long addressId);

}
