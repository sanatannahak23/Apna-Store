package com.apnaStore.product_catalog_service.service.services;

import com.apnaStore.product_catalog_service.dto.request.CategoryRequest;
import com.apnaStore.product_catalog_service.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest categoryRequest);

    List<CategoryResponse> getAllCategory(int page, int size, String sortBy, String sortDir);

    CategoryResponse getCategoryById(Long id);

    CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest);

    void deleteCategory(Long id);
}
