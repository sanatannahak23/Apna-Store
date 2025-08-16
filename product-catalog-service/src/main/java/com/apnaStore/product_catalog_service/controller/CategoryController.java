package com.apnaStore.product_catalog_service.controller;

import com.apnaStore.product_catalog_service.dto.request.CategoryRequest;
import com.apnaStore.product_catalog_service.dto.response.ApiResponse;
import com.apnaStore.product_catalog_service.messages.SuccessMessages;
import com.apnaStore.product_catalog_service.service.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/products/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    //    POST /api/categories(Create Category)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> createCategory(
            @RequestPart("categoryRequest") CategoryRequest categoryRequest,
            @RequestPart("file") MultipartFile file) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse(Boolean.FALSE,
                        categoryService.createCategory(categoryRequest, file),
                        SuccessMessages.CATEGORY_ADDED_SUCCESSFULLY)
                );
    }

    //    GET /api/categories(Get All Categories)
    @GetMapping
    public ResponseEntity<ApiResponse> getAllCategories(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size,
                                                        @RequestParam(defaultValue = "name") String sortBy,
                                                        @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(Boolean.FALSE,
                        categoryService.getAllCategory(page, size, sortBy, sortDir),
                        SuccessMessages.DATA_FETCHED_SUCCESSFULLY));
    }

    //    GET /api/categories/{id}(Get Category by ID)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getCategoryId(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(Boolean.FALSE,
                        categoryService.getCategoryById(id),
                        SuccessMessages.CATEGORY_ADDED_SUCCESSFULLY));
    }

    //    PUT /api/categories/{id}`(Update Category)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable("id") Long id, @RequestBody CategoryRequest categoryRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse(Boolean.FALSE,
                        categoryService.updateCategory(id, categoryRequest),
                        SuccessMessages.CATEGORY_UPDATED_SUCCESSFULLY));
    }

    @PutMapping(value = "/category-image/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> updateCategoryImage(
            @PathVariable("id") Long id,
            @RequestPart("file") MultipartFile file) {
        categoryService.updateCategoryImage(id, file);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse(Boolean.FALSE,
                        null,
                        SuccessMessages.CATEGORY_UPDATED_SUCCESSFULLY)
                );
    }

    //    DELETE /api/categories/{id}`(Delete Category)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse(Boolean.FALSE,
                        null,
                        SuccessMessages.CATEGORY_DELETED_SUCCESSFULLY));
    }

}
