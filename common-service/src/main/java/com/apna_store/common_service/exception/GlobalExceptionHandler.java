package com.apna_store.common_service.exception;

import com.apna_store.common_service.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FileDownloadException.class)
    public ResponseEntity<ApiResponse> exceptionHandler(FileDownloadException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(Boolean.TRUE,
                        null,
                        ex.getMessage()));
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ApiResponse> exceptionHandler(FileUploadException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(Boolean.TRUE,
                        null,
                        ex.getMessage()));
    }

    @ExceptionHandler(FileReadException.class)
    public ResponseEntity<ApiResponse> exceptionHandler(FileReadException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(Boolean.TRUE,
                        null,
                        ex.getMessage()));
    }

    @ExceptionHandler(DataFoundException.class)
    public ResponseEntity<ApiResponse> exceptionHandler(DataFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(Boolean.TRUE,
                        null,
                        ex.getMessage()));
    }

    @ExceptionHandler(FileDeleteException.class)
    public ResponseEntity<ApiResponse> exceptionHandler(FileDeleteException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(Boolean.TRUE,
                        null,
                        ex.getMessage()));
    }
}
