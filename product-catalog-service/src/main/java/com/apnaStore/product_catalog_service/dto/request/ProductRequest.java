package com.apnaStore.product_catalog_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    private Long productCategoryId;

    private String name;

    private BigDecimal price;

    private String description;

    private List<ProductAttributeRequest> attributes;
}
