package com.apnaStore.inventory_service.helper;

import com.apnaStore.inventory_service.dto.request.WarehouseRequest;
import com.apnaStore.inventory_service.entities.Warehouse;

public class RequestToEntity {

    public static Warehouse requestToWarehouse(WarehouseRequest warehouseRequest) {
        Warehouse warehouse = new Warehouse();
        warehouse.setName(warehouseRequest.getName());
        warehouse.setLocation(warehouseRequest.getLocation());
        return warehouse;
    }
}
