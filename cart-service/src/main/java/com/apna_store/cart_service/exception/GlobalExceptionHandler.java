package com.apna_store.cart_service.exception;

import com.apna_store.cart_service.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataAlreadyExistException.class)
    public ResponseEntity<ApiResponse> exceptionHandle(DataAlreadyExistException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(Boolean.TRUE, null, ex.getMessage()));
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ApiResponse> exceptionHandle(DataNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(Boolean.TRUE, null, ex.getMessage()));
    }

    @ExceptionHandler(ProductNotFound.class)
    public ResponseEntity<ApiResponse> exceptionHandle(ProductNotFound ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(Boolean.TRUE, null, ex.getMessage()));
    }
}
