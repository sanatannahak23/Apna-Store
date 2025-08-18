package com.apnaStore.product_catalog_service.exception;

import com.apnaStore.product_catalog_service.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(exception = DataNotFoundException.class)
    public ResponseEntity<ApiResponse> handleException(DataNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(Boolean.TRUE,
                        null,
                        exception.getMessage()));
    }

    @ExceptionHandler(exception = DataAlreadyExist.class)
    public ResponseEntity<ApiResponse> handleException(DataAlreadyExist exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(Boolean.TRUE,
                        null,
                        exception.getMessage()));
    }

    @ExceptionHandler(exception = FileUploadException.class)
    public ResponseEntity<ApiResponse> handleException(FileUploadException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(Boolean.TRUE,
                        null,
                        exception.getMessage()));
    }

    @ExceptionHandler(exception = FileDownloadException.class)
    public ResponseEntity<ApiResponse> handleException(FileDownloadException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(Boolean.TRUE,
                        null,
                        exception.getMessage()));
    }

    @ExceptionHandler(exception = FileException.class)
    public ResponseEntity<ApiResponse> handleException(FileException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(Boolean.TRUE,
                        null,
                        exception.getMessage()));
    }

    @ExceptionHandler(exception = FileSizeException.class)
    public ResponseEntity<ApiResponse> handleException(FileSizeException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(Boolean.TRUE,
                        null,
                        exception.getMessage()));
    }
}
