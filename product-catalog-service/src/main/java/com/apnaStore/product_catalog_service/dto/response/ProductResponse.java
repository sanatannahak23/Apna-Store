package com.apnaStore.product_catalog_service.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductResponse {

    private Long id;

    private String prodRef;

    private String name;

    private String description;

    private BigDecimal price;

    private String category;

    private List<ProductAttributeResponse> attributes;

    private List<byte[]> images;
}
