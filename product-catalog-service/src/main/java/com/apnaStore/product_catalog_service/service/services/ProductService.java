package com.apnaStore.product_catalog_service.service.services;

import com.apnaStore.product_catalog_service.dto.request.ProductRequest;
import com.apnaStore.product_catalog_service.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest productRequest);

    List<ProductResponse> getAllProducts(int page, int size, String sortBy, String sortDir);

    ProductResponse getById(Long id);

    ProductResponse updateProduct(Long id, ProductRequest productRequest);

    void deleteProduct(Long id);

    List<ProductResponse> getProductByCategory(Long categoryId);

    List<ProductResponse> searchProduct(String search, int page, int size, String sortBy, String sortDir);
}
