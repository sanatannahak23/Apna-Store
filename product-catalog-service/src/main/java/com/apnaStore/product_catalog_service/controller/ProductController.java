package com.apnaStore.product_catalog_service.controller;

import com.apnaStore.product_catalog_service.dto.request.ProductRequest;
import com.apnaStore.product_catalog_service.dto.response.ApiResponse;
import com.apnaStore.product_catalog_service.messages.SuccessMessages;
import com.apnaStore.product_catalog_service.service.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    //    POST /api/products (create product)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> createProduct(@RequestPart("productRequest") ProductRequest productRequest,
                                                     @RequestPart("file") MultipartFile[] file) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse(Boolean.FALSE,
                        productService.createProduct(productRequest, file),
                        SuccessMessages.PRODUCT_ADDED_SUCCESSFULLY));
    }

    //    GET /api/products/{id}  (get product by id)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(Boolean.FALSE,
                        productService.getById(id),
                        SuccessMessages.DATA_FETCHED_SUCCESSFULLY));
    }

    //    GET /api/products  (list all products)
    @GetMapping
    public ResponseEntity<ApiResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(defaultValue = "name") String sortBy,
                                              @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse(Boolean.FALSE,
                        productService.getAllProducts(page, size, sortBy, sortDir),
                        SuccessMessages.DATA_FETCHED_SUCCESSFULLY));
    }

    //    PUT /api/products/{id} (update product)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable("id") Long id, @RequestBody ProductRequest productRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(Boolean.FALSE,
                        productService.updateProduct(id, productRequest),
                        SuccessMessages.DATA_UPDATED_SUCCESSFULLY));
    }

    //    DELETE /api/products/{id}  (delete product)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(Boolean.FALSE,
                        null,
                        SuccessMessages.DATA_REMOVED_SUCCESSFULLY));
    }

    //    GET /api/products/category/{categoryId}  (get product by categorty)
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse> getProductByCategory(@PathVariable("categoryId") Long categoryId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(Boolean.FALSE,
                        productService.getProductByCategory(categoryId),
                        SuccessMessages.DATA_FETCHED_SUCCESSFULLY));
    }

    //    GET /api/products/search?query=shirt  (search product)
    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchProducts(@RequestParam String search,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size,
                                                      @RequestParam(defaultValue = "name") String sortBy,
                                                      @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(Boolean.FALSE,
                        productService.searchProduct(search, page, size, sortBy, sortDir),
                        SuccessMessages.DATA_FETCHED_SUCCESSFULLY));
    }

    //    GET /api/products/{id}/details  (get product with attributes)
    @GetMapping("/{id}/details")
    public ResponseEntity<ApiResponse> getProductWithAttributes(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(null);
    }

    // filter products
    @GetMapping("/filter")
    public ResponseEntity<ApiResponse> filterProducts() {
        return null;
    }

}
