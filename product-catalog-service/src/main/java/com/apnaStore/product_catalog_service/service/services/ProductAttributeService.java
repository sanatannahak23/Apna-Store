package com.apnaStore.product_catalog_service.service.services;

import com.apnaStore.product_catalog_service.dto.request.ProductAttributeRequest;
import com.apnaStore.product_catalog_service.dto.response.ProductAttributeResponse;

import java.util.List;

public interface ProductAttributeService {
    ProductAttributeResponse addAttributesInProduct(Long productId, ProductAttributeRequest productAttributeRequest);

    ProductAttributeResponse updateProductAttribute(Long productId, Long attributeId, ProductAttributeRequest productAttributeRequest);

    void removeAttribute(Long productId, Long attributeId);

    List<ProductAttributeResponse> getAttributesByProduct(Long productId);
}
