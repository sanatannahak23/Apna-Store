package com.apnaStore.user_service.exception;

public class InvalidUserNameOrPassword extends RuntimeException {
    public InvalidUserNameOrPassword(String message) {
        super(message);
    }
}
