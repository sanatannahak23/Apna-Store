package com.apnaStore.inventory_service.dto.request;

import lombok.Data;

@Data
public class InventoryRequest {

    private Long productId;

    private String warehouseId;

    private Long stock;
}
