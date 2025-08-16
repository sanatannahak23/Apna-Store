package com.apna_store.common_service.service.services;

import org.springframework.web.multipart.MultipartFile;

public interface S3service {

    String uploadFile(String productKey,MultipartFile file);

    byte[] downloadFile(String key);

    void deleteFile(String key);
}
