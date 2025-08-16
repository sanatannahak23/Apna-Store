package com.apnaStore.inventory_service.services.service;

import com.apnaStore.inventory_service.dto.request.InventoryRequest;
import com.apnaStore.inventory_service.dto.request.StockUpdateRequest;
import com.apnaStore.inventory_service.dto.response.InventoryResponse;

import java.util.List;

public interface InventoryService {

    InventoryResponse createInventory(InventoryRequest inventoryRequest);

    InventoryResponse getInventoryById(String inventoryId);

    List<InventoryResponse> getInventoryByProdId(Long productId);

    Long getTotalStock(Long productId);

    InventoryResponse decreaseStock(StockUpdateRequest stockUpdateRequest);

    InventoryResponse increaseStock(StockUpdateRequest stockUpdateRequest);

    void removeInventory(String inventoryId);

    List<InventoryResponse> getAllInventory(int page, int size, String sortBy, String sortDir);

}
