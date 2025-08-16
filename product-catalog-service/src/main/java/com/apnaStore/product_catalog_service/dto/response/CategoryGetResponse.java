package com.apnaStore.product_catalog_service.dto.response;

import com.apnaStore.product_catalog_service.entities.Auditable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryGetResponse extends Auditable {

    private Long id;

    private String name;

    private byte[] image;

    private String description;

    private List<ProductResponse> products;
}
