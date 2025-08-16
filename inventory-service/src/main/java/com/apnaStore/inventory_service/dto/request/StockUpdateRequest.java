package com.apnaStore.inventory_service.dto.request;

import lombok.Data;

@Data
public class StockUpdateRequest {

    private String inventoryId;

    private Long quantity;
}