package com.apnaStore.product_catalog_service.dto.response;

import com.apnaStore.product_catalog_service.entities.Auditable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryResponse extends Auditable {

    private Long id;

    private String name;

    private String imageRef;

    private String description;

    private List<ProductResponse> products;
}
