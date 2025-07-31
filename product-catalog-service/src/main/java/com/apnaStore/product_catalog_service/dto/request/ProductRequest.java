package com.apnaStore.product_catalog_service.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductRequest {

    private Long productCategoryId;

    private String name;

    private BigDecimal price;

    private String description;

    private List<ProductAttributeRequest> attributes;
}
