package com.apnaStore.product_catalog_service.controller;

import com.apnaStore.product_catalog_service.dto.request.ProductAttributeRequest;
import com.apnaStore.product_catalog_service.dto.response.ApiResponse;
import com.apnaStore.product_catalog_service.messages.SuccessMessages;
import com.apnaStore.product_catalog_service.service.services.ProductAttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class AttributeController {

    private final ProductAttributeService productAttributeService;

    //    POST /api/products/{productId}/attributes(Add Attribute to Product)
    @PostMapping("/{productId}/attributes")
    public ResponseEntity<ApiResponse> addAttributesByProd(@PathVariable("productId") Long productId,
                                                           @RequestBody ProductAttributeRequest productAttributeRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(Boolean.FALSE,
                        productAttributeService.addAttributesInProduct(productId, productAttributeRequest),
                        SuccessMessages.ATTRIBUTE_ADDED_SUCCESSFULLY));
    }

    //    PUT /api/products/{productId}/attributes/{attributeId}(Update Product Attribute)
    @PutMapping("/{productId}/attributes/{attributeId}")
    public ResponseEntity<ApiResponse> updateProductAttribute(@PathVariable("productId") Long productId,
                                                              @PathVariable("attributeId") Long attributeId,
                                                              @RequestBody ProductAttributeRequest productAttributeRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(Boolean.FALSE,
                        productAttributeService.updateProductAttribute(productId, attributeId, productAttributeRequest),
                        SuccessMessages.DATA_UPDATED_SUCCESSFULLY));
    }

    //    DELETE /api/products/{productId}/attributes/{attributeId}   ( Delete Product Attribute)
    @DeleteMapping("/{productId}/attributes/{attributeId}")
    public ResponseEntity<ApiResponse> deleteProductAttributes(@PathVariable("productId") Long productId,
                                                               @PathVariable("attributeId") Long attributeId) {
        productAttributeService.removeAttribute(productId, attributeId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(Boolean.FALSE,
                        null,
                        SuccessMessages.DATA_REMOVED_SUCCESSFULLY));
    }

    //    GET /api/products/{productId}/attributes( Get Attributes of Product)
    @GetMapping("/{productId}/attributes")
    public ResponseEntity<ApiResponse> getAttributesByProd(@PathVariable("productId") Long productId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(Boolean.FALSE,
                        productAttributeService.getAttributesByProduct(productId),
                        SuccessMessages.DATA_FETCHED_SUCCESSFULLY));
    }

}
