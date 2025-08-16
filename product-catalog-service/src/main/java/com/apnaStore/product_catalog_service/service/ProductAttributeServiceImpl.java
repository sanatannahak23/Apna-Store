package com.apnaStore.product_catalog_service.service;

import com.apnaStore.product_catalog_service.dto.request.ProductAttributeRequest;
import com.apnaStore.product_catalog_service.dto.response.ProductAttributeResponse;
import com.apnaStore.product_catalog_service.entities.Product;
import com.apnaStore.product_catalog_service.entities.ProductAttribute;
import com.apnaStore.product_catalog_service.exception.DataNotFoundException;
import com.apnaStore.product_catalog_service.helper.EntityToResponse;
import com.apnaStore.product_catalog_service.helper.RequestToEntity;
import com.apnaStore.product_catalog_service.messages.ExceptionMessages;
import com.apnaStore.product_catalog_service.repository.ProductAttributeRepository;
import com.apnaStore.product_catalog_service.repository.ProductRepository;
import com.apnaStore.product_catalog_service.service.services.ProductAttributeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductAttributeServiceImpl implements ProductAttributeService {

    private final ProductAttributeRepository productAttributeRepository;

    private final ProductRepository productRepository;

    @Override
    public ProductAttributeResponse addAttributesInProduct(Long productId,
                                                           ProductAttributeRequest productAttributeRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.PRODUCT_NOT_FOUND));
        ProductAttribute productAttribute = RequestToEntity.requestToProductAttribute(productAttributeRequest, product);
        productAttribute = productAttributeRepository.save(productAttribute);
        return EntityToResponse.productAttributeToResponse(productAttribute, product);
    }

    @Override
    public ProductAttributeResponse updateProductAttribute(Long productId,
                                                           Long attributeId,
                                                           ProductAttributeRequest productAttributeRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.PRODUCT_NOT_FOUND));
        ProductAttribute productAttribute = productAttributeRepository.findById(attributeId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND));
        if (productAttributeRequest.getKey() != null &&
                !productAttributeRequest.getKey().equals(productAttribute.getKey())) {
            productAttribute.setKey(productAttributeRequest.getKey());
        }

        if (productAttributeRequest.getValue() != null &&
                !productAttributeRequest.getValue().equals(productAttribute.getValue())) {
            productAttribute.setValue(productAttributeRequest.getValue());
        }

        if (product != null) {
            productAttribute.setProduct(product);
        }
        productAttribute = productAttributeRepository.save(productAttribute);
        log.info("Updated product attribute :: {}", productAttributeRequest.getValue());
        return EntityToResponse.productAttributeToResponse(productAttribute, product);
    }

    @Override
    public void removeAttribute(Long productId, Long attributeId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.PRODUCT_NOT_FOUND));
        ProductAttribute productAttribute = productAttributeRepository.findById(attributeId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND));
        productAttributeRepository.delete(productAttribute);
    }

    @Override
    public List<ProductAttributeResponse> getAttributesByProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.PRODUCT_NOT_FOUND));
        List<ProductAttribute> productAttributes = product.getAttributes();
        return productAttributes
                .stream()
                .map(productAttribute -> EntityToResponse
                        .productAttributeToResponse(productAttribute, product))
                .toList();
    }
}
