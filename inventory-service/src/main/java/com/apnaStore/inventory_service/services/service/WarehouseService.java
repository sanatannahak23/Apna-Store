package com.apnaStore.inventory_service.services.service;

import com.apnaStore.inventory_service.dto.request.WarehouseRequest;
import com.apnaStore.inventory_service.dto.response.WarehouseResponse;

import java.util.List;

public interface WarehouseService {

    void deleteWarehouse(String id);

    WarehouseResponse updateWarehouse(String id, WarehouseRequest warehouseRequest);

    WarehouseResponse getById(String id);

    List<WarehouseResponse> getAll(int page, int size, String sortBy, String sortDir);

    WarehouseResponse create(WarehouseRequest warehouseRequest);
}
