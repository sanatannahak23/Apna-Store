package com.apnaStore.user_service.exception;

public class RefreshTokenExpireException extends RuntimeException {
    public RefreshTokenExpireException(String message) {
        super(message);
    }
}
