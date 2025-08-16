package com.apnaStore.inventory_service.exception;

import com.apnaStore.inventory_service.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.UnknownHostException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataAlreadyExist.class)
    public ResponseEntity<ApiResponse> handleException(DataAlreadyExist ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(Boolean.TRUE,
                null,
                ex.getMessage()));
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ApiResponse> handleException(DataNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(Boolean.TRUE,
                null,
                ex.getMessage()));
    }

    @ExceptionHandler(ProductNotFound.class)
    public ResponseEntity<ApiResponse> handleException(ProductNotFound ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(Boolean.TRUE,
                null,
                ex.getMessage()));
    }

    @ExceptionHandler(UnknownHostException.class)
    public ResponseEntity<String> handleUnknownHostException(UnknownHostException ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body("Could not resolve host: " + ex.getMessage());
    }
}
