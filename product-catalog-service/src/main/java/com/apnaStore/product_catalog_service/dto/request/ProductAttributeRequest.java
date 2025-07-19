package com.apnaStore.product_catalog_service.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductAttributeRequest {

    private String key;

    private String value;
}
