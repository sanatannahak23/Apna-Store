package com.apnaStore.product_catalog_service.service;

import com.apnaStore.product_catalog_service.dto.request.ProductRequest;
import com.apnaStore.product_catalog_service.dto.response.ProductAttributeResponse;
import com.apnaStore.product_catalog_service.dto.response.ProductResponse;
import com.apnaStore.product_catalog_service.entities.Category;
import com.apnaStore.product_catalog_service.entities.Product;
import com.apnaStore.product_catalog_service.entities.ProductAttribute;
import com.apnaStore.product_catalog_service.exception.DataAlreadyExist;
import com.apnaStore.product_catalog_service.exception.DataNotFoundException;
import com.apnaStore.product_catalog_service.helper.EntityToResponse;
import com.apnaStore.product_catalog_service.helper.RequestToEntity;
import com.apnaStore.product_catalog_service.messages.ExceptionMessages;
import com.apnaStore.product_catalog_service.repository.CategoryRepository;
import com.apnaStore.product_catalog_service.repository.ProductAttributeRepository;
import com.apnaStore.product_catalog_service.repository.ProductRepository;
import com.apnaStore.product_catalog_service.service.services.ProductAttributeService;
import com.apnaStore.product_catalog_service.service.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final ProductAttributeRepository productAttributeRepository;

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        productRepository.findByName(productRequest.getName())
                .ifPresent(ex -> {
                    throw new DataAlreadyExist(ExceptionMessages.PRODUCT_ALREADY_EXIST);
                });
        Category category = categoryRepository.findById(productRequest.getProductCategoryId())
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.PRODUCT_CATEGORY_NOT_FOUND));
        log.info("The Category for the product :: {}", category);
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
        return products
                .stream()
                .map(EntityToResponse::productToResponse)
                .toList();
    }

    @Override
    public ProductResponse getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.PRODUCT_NOT_FOUND));
        return EntityToResponse.productToResponse(product);
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
        productRepository.delete(product);
    }

    @Override
    public List<ProductResponse> getProductByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND));
        List<Product> products = productRepository.findByCategory(category);
        if (products.isEmpty()) throw new DataNotFoundException(ExceptionMessages.PRODUCT_NOT_FOUND);
        return products
                .stream()
                .map(EntityToResponse::productToResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> searchProduct(String search, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        List<Product> products = productRepository.searchByNameAndCategory(pageable, search);
        return products
                .stream()
                .map(EntityToResponse::productToResponse)
                .toList();
    }
}
