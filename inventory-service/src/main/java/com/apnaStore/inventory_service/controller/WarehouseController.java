package com.apnaStore.inventory_service.controller;

import com.apnaStore.inventory_service.dto.request.WarehouseRequest;
import com.apnaStore.inventory_service.dto.response.ApiResponse;
import com.apnaStore.inventory_service.messages.SuccessMessages;
import com.apnaStore.inventory_service.services.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory/warehouse")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    // POST /api/warehouses (Create warehouse)
    @PostMapping
    public ResponseEntity<ApiResponse> createWarehouse(@RequestBody WarehouseRequest warehouseRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse(Boolean.FALSE,
                        warehouseService.create(warehouseRequest),
                        SuccessMessages.WAREHOUSE_CREATED_SUCCESSFULLY));
    }

    // GET /api/warehouses (Get all warehouse)
    @GetMapping
    public ResponseEntity<ApiResponse> getAllWarehouse(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam(defaultValue = "name") String sortBy,
                                                       @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(Boolean.FALSE,
                        warehouseService.getAll(page, size, sortBy, sortDir),
                        SuccessMessages.DATA_FETCHED_SUCCESSFULLY));
    }

    // GET /api/warehouses/{id} (Get warehouse by id)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getWarehouseById(@PathVariable("id") String id) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse(Boolean.FALSE,
                        warehouseService.getById(id),
                        SuccessMessages.DATA_FETCHED_SUCCESSFULLY));
    }

    // PUT /api/warehouses/{id} (Update warehouse)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateWarehouse(@PathVariable("id") String id, @RequestBody WarehouseRequest warehouseRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(Boolean.FALSE,
                        warehouseService.updateWarehouse(id, warehouseRequest),
                        SuccessMessages.DATA_UPDATED_SUCCESSFULLY));
    }

    // DELETE /api/warehouses/{id} (Delete warehouse)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteWarehouse(@PathVariable("id") String id) {
        warehouseService.deleteWarehouse(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(Boolean.FALSE,
                        null,
                        SuccessMessages.DATA_REMOVED_SUCCESSFULLY));
    }
}
