package com.apnaStore.product_catalog_service.helper;

import com.apnaStore.product_catalog_service.dto.response.CategoryGetResponse;
import com.apnaStore.product_catalog_service.dto.response.CategoryResponse;
import com.apnaStore.product_catalog_service.dto.response.ProductAttributeResponse;
import com.apnaStore.product_catalog_service.dto.response.ProductResponse;
import com.apnaStore.product_catalog_service.entities.Category;
import com.apnaStore.product_catalog_service.entities.Product;
import com.apnaStore.product_catalog_service.entities.ProductAttribute;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EntityToResponse {

    public static CategoryResponse categoryToResponse(Category category) {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(category.getId());
        categoryResponse.setName(category.getName());
        categoryResponse.setDescription(category.getDescription());
        categoryResponse.setCreatedBy(category.getCreatedBy());
        categoryResponse.setCreatedDate(category.getCreatedDate());
        categoryResponse.setLastModifiedBy(category.getLastModifiedBy());
        categoryResponse.setLastModifiedDate(category.getLastModifiedDate());
        categoryResponse.setImageRef(category.getImageRef());
        return categoryResponse;
    }

    public static CategoryGetResponse categoryToCategoryGetResponse(Category category) {
        CategoryGetResponse response = new CategoryGetResponse();
        response.setId(category.getId());
        response.setDescription(category.getDescription());
        response.setName(category.getName());
        response.setCreatedBy(category.getCreatedBy());
        response.setCreatedDate(category.getCreatedDate());
        response.setLastModifiedBy(category.getLastModifiedBy());
        response.setLastModifiedDate(category.getLastModifiedDate());
        response.setProducts(null);
        return response;
    }

    public static ProductResponse productToResponse(Product product) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setDescription(product.getDescription());
        productResponse.setName(product.getName());
        productResponse.setId(product.getId());
        productResponse.setProdRef(product.getProdRef());
        productResponse.setPrice(product.getPrice());
        productResponse.setCategory(product.getCategory().getName());
        productResponse.setAttributes(product.getAttributes()
                .stream()
                .map(productAttribute -> productAttributeToResponse(productAttribute, product))
                .toList());
        return productResponse;
    }

    public static ProductAttributeResponse productAttributeToResponse(ProductAttribute productAttribute, Product productFinal) {
        ProductAttributeResponse productAttributeResponse = new ProductAttributeResponse();
        productAttributeResponse.setId(productAttribute.getId());
        productAttributeResponse.setKey(productAttribute.getKey());
        productAttributeResponse.setValue(productAttribute.getValue());
        productAttributeResponse.setProductName(productAttribute.getProduct().getName());
        return productAttributeResponse;
    }
}
