package com.apnaStore.product_catalog_service.service.services;

import com.apnaStore.product_catalog_service.dto.request.CategoryRequest;
import com.apnaStore.product_catalog_service.dto.response.CategoryGetResponse;
import com.apnaStore.product_catalog_service.dto.response.CategoryResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest categoryRequest, MultipartFile file);

    List<CategoryGetResponse> getAllCategory(int page, int size, String sortBy, String sortDir);

    CategoryGetResponse getCategoryById(Long id);

    CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest);

    void deleteCategory(Long id);

    void updateCategoryImage(Long id, MultipartFile file);
}
