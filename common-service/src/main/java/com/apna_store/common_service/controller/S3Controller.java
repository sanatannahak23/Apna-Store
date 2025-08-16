package com.apna_store.common_service.controller;

import com.apna_store.common_service.dto.response.ApiResponse;
import com.apna_store.common_service.messages.SuccessMessages;
import com.apna_store.common_service.service.services.S3service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/s3-service")
@RequiredArgsConstructor
public class S3Controller {

    private final S3service s3service;

    // upload file
    @PostMapping("/upload/{productKey}")
    public ResponseEntity<ApiResponse> uploadFile(
            @PathVariable String productKey,
            @RequestPart("file") MultipartFile file
    ) {
        log.info("upload api called :: {}", productKey);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(Boolean.FALSE,
                        s3service.uploadFile(productKey, file),
                        SuccessMessages.FILE_UPLOADED_SUCCESSFULLY + " :: " + productKey));
    }

    // Upload multiple files
    @PostMapping("/upload-multiple/{productKey}")
    public ResponseEntity<String> uploadMultipleFiles(
            @PathVariable String productKey,
            @RequestParam("files") List<MultipartFile> files
    ) {
        files.forEach(file -> s3service.uploadFile(productKey, file));
        return ResponseEntity.ok(files.size() + SuccessMessages.FILE_UPLOADED_SUCCESSFULLY + " :: " + productKey);
    }

    // Download file
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam String key) {
        return ResponseEntity
                .ok()
                .body(s3service.downloadFile(key));
    }

    // Delete file
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteFile(@RequestParam String key) {
        s3service.deleteFile(key);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(Boolean.FALSE,
                        null,
                        SuccessMessages.FILE_DELETED_SUCCESSFULLY + " :: " + key));
    }
}
