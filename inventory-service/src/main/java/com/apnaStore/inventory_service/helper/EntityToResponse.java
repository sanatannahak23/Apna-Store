package com.apnaStore.inventory_service.helper;

import com.apnaStore.inventory_service.dto.response.InventoryResponse;
import com.apnaStore.inventory_service.dto.response.WarehouseResponse;
import com.apnaStore.inventory_service.entities.Inventory;
import com.apnaStore.inventory_service.entities.Warehouse;

public class EntityToResponse {

    public static WarehouseResponse warehouseToResponse(Warehouse warehouse) {
        WarehouseResponse warehouseResponse = new WarehouseResponse();
        warehouseResponse.setId(warehouse.getId());
        warehouseResponse.setName(warehouse.getName());
        warehouseResponse.setLocation(warehouse.getLocation());
        warehouseResponse.setCreatedAt(warehouse.getCreatedAt());
        warehouseResponse.setCreatedBy(warehouse.getCreatedBy());
        warehouseResponse.setLastModifiedAt(warehouse.getLastModifiedAt());
        warehouseResponse.setLastModifiedBy(warehouse.getLastModifiedBy());
        return warehouseResponse;
    }

    public static InventoryResponse inventoryToResponse(Inventory inventory) {
        InventoryResponse inventoryResponse = new InventoryResponse();
        inventoryResponse.setId(inventory.getId());
        inventoryResponse.setStock(inventory.getStock());
        inventoryResponse.setProductId(inventory.getProductId());
        inventoryResponse.setWarehouseName(inventory.getWarehouse().getName());
        inventoryResponse.setCreatedAt(inventory.getCreatedAt());
        inventoryResponse.setCreatedBy(inventory.getCreatedBy());
        inventoryResponse.setLastModifiedAt(inventory.getLastModifiedAt());
        inventoryResponse.setLastModifiedBy(inventory.getLastModifiedBy());
        return inventoryResponse;
    }
}
