package com.apna_store.common_service.service;

import com.apna_store.common_service.entities.UploadedFile;
import com.apna_store.common_service.entities.enums.ReferenceType;
import com.apna_store.common_service.exception.*;
import com.apna_store.common_service.messages.ExceptionMessages;
import com.apna_store.common_service.repository.UploadFileRepository;
import com.apna_store.common_service.service.services.S3service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3serviceImpl implements S3service {

    private final S3Client s3Client;

    private final UploadFileRepository uploadFileRepository;

    @Value("${spring.aws.bucket}")
    private String bucketName;

    @Override
    public String uploadFile(String key, MultipartFile file) {
        try {
            log.info("Bucket name :: {}", bucketName);
            key = key + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

            log.info("Received key :: {}", key);
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
            log.info("File uploaded successfully: {} ", key);
            UploadedFile uploadedFile = new UploadedFile();
            uploadedFile.setFileSize(file.getSize());
            uploadedFile.setOriginalFileName(file.getName());
            uploadedFile.setS3Key(key);
            uploadedFile.setContentType(file.getContentType());
            log.info("Ref type :: {}", key.split("/")[0]);
            ReferenceType referenceType = ReferenceType.type(key.split("/")[0].toLowerCase());
            uploadedFile.setReferenceType(referenceType == null ? ReferenceType.OTHERS : referenceType);

            uploadFileRepository.findByS3Key(key)
                    .ifPresent(ex -> {
                        throw new DataFoundException(ExceptionMessages.KEY_ALREADY_EXIST);
                    });
            uploadedFile = uploadFileRepository.save(uploadedFile);
            return uploadedFile.getReferenceId();
        } catch (IOException e) {
            throw new FileReadException(ExceptionMessages.ERROR_READING_FILE + " :: " + e.getMessage());
        } catch (S3Exception e) {
            throw new FileUploadException(ExceptionMessages.ERROR_UPLOADING_FILE_TO_S3 + " :: " + e.getMessage());
        }
    }

    @Override
    public byte[] downloadFile(String key) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            return s3Client.getObjectAsBytes(getObjectRequest).asByteArray();

        } catch (S3Exception e) {
            throw new FileDownloadException(ExceptionMessages.ERROR_DOWNLOADING_FILE + " :: " + e.getMessage());
        }
    }

    @Override
    public void deleteFile(String key) {
        try {
            s3Client.deleteObject(builder -> builder
                    .bucket(bucketName)
                    .key(key)
                    .build());

            log.info("File deleted successfully :: {}", key);
            UploadedFile uploadedFile = uploadFileRepository.findByS3Key(key)
                    .orElseThrow(() -> new DataNotFound(ExceptionMessages.DATA_NOT_FOUND));
            uploadFileRepository.delete(uploadedFile);
        } catch (S3Exception e) {
            throw new FileDeleteException(ExceptionMessages.ERROR_DELETING_FILE + " :: " + e.getMessage());
        }
    }

}
