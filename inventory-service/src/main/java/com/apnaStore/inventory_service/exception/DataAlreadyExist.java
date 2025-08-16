package com.apnaStore.inventory_service.exception;

public class DataAlreadyExist extends RuntimeException {
    public DataAlreadyExist(String message) {
        super(message);
    }
}
