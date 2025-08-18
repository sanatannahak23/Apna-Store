package com.apnaStore.product_catalog_service.service;

import com.apnaStore.product_catalog_service.clients.CommonClient;
import com.apnaStore.product_catalog_service.dto.request.CategoryRequest;
import com.apnaStore.product_catalog_service.dto.response.ApiResponse;
import com.apnaStore.product_catalog_service.dto.response.CategoryGetResponse;
import com.apnaStore.product_catalog_service.dto.response.CategoryResponse;
import com.apnaStore.product_catalog_service.entities.Category;
import com.apnaStore.product_catalog_service.entities.Product;
import com.apnaStore.product_catalog_service.entities.UploadedFile;
import com.apnaStore.product_catalog_service.entities.enums.ReferenceType;
import com.apnaStore.product_catalog_service.exception.*;
import com.apnaStore.product_catalog_service.helper.EntityToResponse;
import com.apnaStore.product_catalog_service.helper.RequestToEntity;
import com.apnaStore.product_catalog_service.kafka.KafkaProducerService;
import com.apnaStore.product_catalog_service.messages.ExceptionMessages;
import com.apnaStore.product_catalog_service.repository.CategoryRepository;
import com.apnaStore.product_catalog_service.repository.UploadFileRepository;
import com.apnaStore.product_catalog_service.service.services.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final ObjectMapper objectMapper;

    private final CommonClient commonClient;

    private final UploadFileRepository uploadFileRepository;

    private final KafkaProducerService kafkaProducerService;

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest categoryRequest, MultipartFile file) {
        categoryRepository.findByName(categoryRequest.getName())
                .ifPresent(ex -> {
                    throw new DataAlreadyExist(ExceptionMessages.CATEGORY_ALREADY_PRESENT);
                });
        Category category = RequestToEntity.requestToCategory(categoryRequest);
        // Need to impl resilence4j
        try {
            ApiResponse response = commonClient.uploadFile(ReferenceType.CATEGORY.toString(), file);
            log.info("Received response :: {}", response.getData() != null ? response.getData() : null);

            String imageRef = objectMapper.convertValue(response.getData(), String.class);
            category.setImageRef(imageRef);
        } catch (Exception ex) {
            log.error("Exception :: {}", ex.getMessage());
            throw new FileUploadException(ex.getMessage());
        }
        category = categoryRepository.save(category);
        log.info("Saved Category :: {}", category);
        return EntityToResponse.categoryToResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryGetResponse> getAllCategory(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        List<Category> content = categoryRepository.findAll(pageable).getContent();
        if (content.isEmpty())
            throw new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND);
        return content
                .stream()
                .map(category -> {
                    CategoryGetResponse response = EntityToResponse.categoryToCategoryGetResponse(category);
                    if (category.getImageRef() != null) {
                        try {
                            log.info("Category Id :: {}", category.getId());
                            UploadedFile uploadedFile = uploadFileRepository.findByReferenceId(category.getImageRef())
                                    .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.INVALID_IMAGE_REF));
                            byte[] bytes = commonClient.downloadFile(uploadedFile.getS3Key());
                            response.setImage(bytes);
                        } catch (Exception ex) {
                            log.error("Exception :: {}", ex.getMessage());
                            throw new FileDownloadException(ex.getMessage());
                        }
                    }
                    return response;
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryGetResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND));
        CategoryGetResponse response = EntityToResponse.categoryToCategoryGetResponse(category);
        if (category.getImageRef() != null) {
            try {
                UploadedFile uploadedFile = uploadFileRepository.findByReferenceId(category.getImageRef())
                        .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.INVALID_IMAGE_REF));
                byte[] bytes = commonClient.downloadFile(uploadedFile.getS3Key());
                response.setImage(bytes);
            } catch (Exception ex) {
                log.error("Exception :: {}", ex.getMessage());
                throw new FileDownloadException(ex.getMessage());
            }
        }
        return response;
    }

    @Override
    @Transactional
    public void updateCategoryImage(Long id, MultipartFile file) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND));

        UploadedFile uploadedFile = uploadFileRepository.findByReferenceId(category.getImageRef())
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.INVALID_IMAGE_REF));

        try {
            commonClient.deleteFile(uploadedFile.getS3Key());
            ApiResponse response = commonClient.uploadFile(ReferenceType.CATEGORY.toString(), file);
            log.info("Received response :: {}", response.getData() != null ? response.getData() : null);
            String imageRef = objectMapper.convertValue(response.getData(), String.class);
            category.setImageRef(imageRef);
        } catch (Exception ex) {
            log.error("Exception :: {}", ex.getMessage());
            throw new FileUploadException(ex.getMessage());
        }
        categoryRepository.save(category);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND));

        log.info("Updated id :: {}", id);
        category.setName(categoryRequest.getName() != null
                ? categoryRequest.getName()
                : category.getName());
        category.setDescription(categoryRequest.getDescription() != null
                ? categoryRequest.getDescription()
                : category.getDescription());
        return EntityToResponse.categoryToResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND));
        log.info("Category Deleted Successfully :: {}", category.getName());

        // Inventory need to be delete :: kafka ✅
        for (Product product : category.getProducts()) {
            log.info("The product Id ::{}", product.getId());
            kafkaProducerService.messageForDeleteInventory(product.getId());
        }

        // image should be delete from s3 ✅
        if (category.getImageRef() != null) {
            UploadedFile uploadedFile = uploadFileRepository.findByReferenceId(category.getImageRef())
                    .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.INVALID_IMAGE_REF));

            try {
                ApiResponse response = commonClient.deleteFile(uploadedFile.getS3Key());
                String message = objectMapper.convertValue(response.getMessage(), String.class);
                log.info(message);
            } catch (Exception ex) {
                log.error("Exception :: {}", ex.getMessage());
                throw new FileException(ExceptionMessages.FAILED_TO_DELETE_FILE + ex.getMessage());
            }
        }
        categoryRepository.delete(category);
    }
}
