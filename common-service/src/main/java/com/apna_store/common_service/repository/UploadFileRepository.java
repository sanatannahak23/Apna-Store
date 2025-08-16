package com.apna_store.common_service.repository;

import com.apna_store.common_service.entities.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UploadFileRepository extends JpaRepository<UploadedFile, Long> {

    Optional<UploadedFile> findByS3Key(String s3Key);
}
