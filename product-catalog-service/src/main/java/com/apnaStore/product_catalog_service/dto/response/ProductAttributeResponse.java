package com.apnaStore.product_catalog_service.dto.response;

import lombok.Data;

@Data
public class ProductAttributeResponse {

    private Long id;

    private String key;

    private String value;

    private String productName;
}
