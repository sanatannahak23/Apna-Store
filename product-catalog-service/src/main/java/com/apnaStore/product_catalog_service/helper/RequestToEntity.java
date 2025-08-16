package com.apnaStore.product_catalog_service.helper;

import com.apnaStore.product_catalog_service.dto.request.CategoryRequest;
import com.apnaStore.product_catalog_service.dto.request.ProductAttributeRequest;
import com.apnaStore.product_catalog_service.dto.request.ProductRequest;
import com.apnaStore.product_catalog_service.entities.Category;
import com.apnaStore.product_catalog_service.entities.Product;
import com.apnaStore.product_catalog_service.entities.ProductAttribute;

public class RequestToEntity {

    public static Category requestToCategory(CategoryRequest categoryRequest) {
        Category category = new Category();
        category.setDescription(categoryRequest.getDescription());
        category.setName(categoryRequest.getName());
        return category;
    }

    public static Product requestToProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
//        product.setAttributes(productRequest.getAttributes()
//                .stream()
//                .map(RequestToEntity::requestToProductAttribute)
//                .toList());
        return product;
    }

    public static ProductAttribute requestToProductAttribute(ProductAttributeRequest productAttributeRequest,Product product) {
        ProductAttribute productAttribute = new ProductAttribute();
        productAttribute.setProduct(product);
        productAttribute.setKey(productAttributeRequest.getKey());
        productAttribute.setValue(productAttributeRequest.getValue());
        return productAttribute;
    }
}
