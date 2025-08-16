package com.apnaStore.product_catalog_service.exception;

public class DataAlreadyExist extends RuntimeException {
    public DataAlreadyExist(String message) {
        super(message);
    }
}
