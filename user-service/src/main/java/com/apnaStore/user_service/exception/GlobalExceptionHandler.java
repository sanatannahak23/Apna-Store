package com.apnaStore.user_service.exception;

import com.apnaStore.user_service.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(exception = DataFoundException.class)
    public ResponseEntity<ApiResponse> handleException(DataFoundException exception) {
        return ResponseEntity
                .badRequest()
                .body(new ApiResponse(
                        Boolean.TRUE,
                        null,
                        exception.getMessage()
                ));
    }

    @ExceptionHandler(exception = DataNotFound.class)
    public ResponseEntity<ApiResponse> handleException(DataNotFound exception) {
        return ResponseEntity
                .badRequest()
                .body(new ApiResponse(
                        Boolean.TRUE,
                        null,
                        exception.getMessage()
                ));
    }

    @ExceptionHandler(exception = InvalidUserNameOrPassword.class)
    public ResponseEntity<ApiResponse> handleException(InvalidUserNameOrPassword exception) {
        return ResponseEntity
                .badRequest()
                .body(new ApiResponse(
                        Boolean.TRUE,
                        null,
                        exception.getMessage()
                ));
    }

    @ExceptionHandler(exception = RefreshTokenExpireException.class)
    public ResponseEntity<ApiResponse> handleException(RefreshTokenExpireException exception) {
        return ResponseEntity
                .badRequest()
                .body(new ApiResponse(
                        Boolean.TRUE,
                        null,
                        exception.getMessage()
                ));
    }

    @ExceptionHandler(exception = InvalidTokenException.class)
    public ResponseEntity<ApiResponse> handleException(InvalidTokenException exception) {
        return ResponseEntity
                .badRequest()
                .body(new ApiResponse(
                        Boolean.TRUE,
                        null,
                        exception.getMessage()
                ));
    }
}
