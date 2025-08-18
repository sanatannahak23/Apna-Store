package com.apnaStore.product_catalog_service.service;

import com.apnaStore.product_catalog_service.clients.CommonClient;
import com.apnaStore.product_catalog_service.dto.request.ProductRequest;
import com.apnaStore.product_catalog_service.dto.response.ApiResponse;
import com.apnaStore.product_catalog_service.dto.response.ProductResponse;
import com.apnaStore.product_catalog_service.entities.*;
import com.apnaStore.product_catalog_service.entities.enums.ReferenceType;
import com.apnaStore.product_catalog_service.exception.*;
import com.apnaStore.product_catalog_service.helper.EntityToResponse;
import com.apnaStore.product_catalog_service.helper.RequestToEntity;
import com.apnaStore.product_catalog_service.kafka.KafkaProducerService;
import com.apnaStore.product_catalog_service.messages.ExceptionMessages;
import com.apnaStore.product_catalog_service.repository.*;
import com.apnaStore.product_catalog_service.service.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final ProductAttributeRepository productAttributeRepository;

    private final ProductImageRepository productImageRepository;

    private final UploadFileRepository uploadFileRepository;

    private final CommonClient commonClient;

    private final ObjectMapper objectMapper;

    private final KafkaProducerService kafkaProducerService;

    @Override
    public ProductResponse createProduct(ProductRequest productRequest, MultipartFile[] file) {
        for (MultipartFile f : file) {
            if (f.getSize() > 2 * 1024 * 1024) {
                throw new FileSizeException(ExceptionMessages.FILE_SIZE_EXCEED + " :: " + f.getSize());
            }
        }

        productRepository.findByName(productRequest.getName())
                .ifPresent(ex -> {
                    throw new DataAlreadyExist(ExceptionMessages.PRODUCT_ALREADY_EXIST);
                });
        Category category = categoryRepository.findById(productRequest.getProductCategoryId())
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.PRODUCT_CATEGORY_NOT_FOUND));
        log.info("The Category for the product :: {}", category.getName());
        Product product = RequestToEntity.requestToProduct(productRequest);
        product.setCategory(category);
        product = productRepository.save(product);

        Product finalProduct = product;
        List<ProductAttribute> productAttributes = productRequest
                .getAttributes()
                .stream()
                .map(productAttributeRequest -> {
                    ProductAttribute productAttribute = new ProductAttribute();
                    productAttribute.setProduct(finalProduct);
                    productAttribute.setKey(productAttributeRequest.getKey());
                    productAttribute.setValue(productAttributeRequest.getValue());
                    return productAttributeRepository.save(productAttribute);
                })
                .toList();
        finalProduct.setAttributes(productAttributes);

        for (MultipartFile f : file) {
            if (f.getSize() < 0) throw new FileSizeException(ExceptionMessages.INVALID_FILE + " :: " + f.getSize());
            try {
                ApiResponse response = commonClient.uploadFile(ReferenceType.PRODUCT.toString() + "/" + product.getName(), f);
                log.info("Received response :: {}", response.getData() != null ? response.getData() : null);
                String imageRef = objectMapper.convertValue(response.getData(), String.class);
                ProductImage productImage = new ProductImage();
                productImage.setImageRef(imageRef);
                productImage.setProduct(product);
                productImageRepository.save(productImage);
            } catch (Exception ex) {
                log.error("Exception :: {}", ex.getMessage());
                throw new FileUploadException(ex.getMessage() + " :: " + f.getName());
            }
        }
        return EntityToResponse.productToResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        List<Product> products = productRepository.findAll(pageable).getContent();
        if (products.isEmpty())
            throw new DataNotFoundException(ExceptionMessages.PRODUCT_NOT_FOUND);
        log.info("All data fetched successfully....");
        ArrayList<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products) {
            productResponses.add(getById(product.getId()));
        }
        return productResponses;
    }

    @Override
    public ProductResponse getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.PRODUCT_NOT_FOUND));
        ProductResponse response = EntityToResponse.productToResponse(product);
        ArrayList<byte[]> bytes = new ArrayList<>();

        for (ProductImage productImage : product.getImages()) {
            try {
                UploadedFile uploadedFile = uploadFileRepository.findByReferenceId(productImage.getImageRef())
                        .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.INVALID_IMAGE_REF));
                byte[] byteData = commonClient.downloadFile(uploadedFile.getS3Key());
                log.info("Byte info :: {}", byteData.length);
                bytes.add(byteData);
            } catch (Exception ex) {
                log.error("Exception :: {}", ex.getMessage());
                throw new FileDownloadException(ex.getMessage());
            }
        }
        if (!bytes.isEmpty()) response.setImages(bytes);
        return response;
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.PRODUCT_NOT_FOUND));

        boolean updated = false;

        if (productRequest.getName() != null && !productRequest.getName().equals(product.getName())) {
            product.setName(productRequest.getName());
            updated = true;
        }

        if (productRequest.getPrice() != null && !productRequest.getPrice().equals(product.getPrice())) {
            product.setPrice(productRequest.getPrice());
            updated = true;
        }

        if (productRequest.getDescription() != null && !productRequest.getDescription().equals(product.getDescription())) {
            product.setDescription(productRequest.getDescription());
            updated = true;
        }

        if (productRequest.getProductCategoryId() != null &&
                (product.getCategory() == null || !product.getCategory().getId().equals(productRequest.getProductCategoryId()))) {
            Category category = categoryRepository.findById(productRequest.getProductCategoryId())
                    .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND));
            product.setCategory(category);
            updated = true;
        }

        if (updated) {
            product = productRepository.save(product);
        }

        return EntityToResponse.productToResponse(product);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.PRODUCT_NOT_FOUND));

        // if i delete the product also inventory should be deleted of that product from all warehouse (kafka)
        log.info("The product Id ::{}", product.getId());
        kafkaProducerService.messageForDeleteInventory(product.getId());

        // Need to delete the images of the product from s3
        for (ProductImage productImage : product.getImages()) {
            UploadedFile uploadedFile = uploadFileRepository.findByReferenceId(productImage.getImageRef())
                    .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.INVALID_IMAGE_REF));

            productImageRepository.delete(productImage);
            log.info("Image key :: {}", uploadedFile.getS3Key());
            try {
                ApiResponse response = commonClient.deleteFile(uploadedFile.getS3Key());
                String message = objectMapper.convertValue(response.getMessage(), String.class);
                log.info(message);
            } catch (Exception ex) {
                log.error("Exception :: {}", ex.getMessage());
                throw new FileException(ExceptionMessages.FAILED_TO_DELETE_FILE + ex.getMessage());
            }
        }
        productRepository.delete(product);
    }

    @Override
    public List<ProductResponse> getProductByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND));
        List<Product> products = productRepository.findByCategory(category);
        if (products.isEmpty()) throw new DataNotFoundException(ExceptionMessages.PRODUCT_NOT_FOUND);

        ArrayList<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products) {
            productResponses.add(getById(product.getId()));
        }
        return productResponses;
    }

    @Override
    public List<ProductResponse> searchProduct(String search, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        List<Product> products = productRepository.searchByNameAndCategory(pageable, search);

        ArrayList<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products) {
            productResponses.add(getById(product.getId()));
        }
        return productResponses;
    }
}
