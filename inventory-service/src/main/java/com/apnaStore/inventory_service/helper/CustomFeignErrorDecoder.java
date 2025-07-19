package com.apnaStore.inventory_service.helper;

import com.apnaStore.inventory_service.exception.ProductNotFound;
import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomFeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 400) {
            return new ProductNotFound("Feign 400 Error: Product not found or invalid input");
        }
        return defaultDecoder.decode(methodKey, response);
    }
}