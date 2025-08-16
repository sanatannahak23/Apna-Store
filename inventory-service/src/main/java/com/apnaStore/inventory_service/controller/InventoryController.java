package com.apnaStore.inventory_service.controller;

import com.apnaStore.inventory_service.dto.request.InventoryRequest;
import com.apnaStore.inventory_service.dto.request.StockUpdateRequest;
import com.apnaStore.inventory_service.dto.response.ApiResponse;
import com.apnaStore.inventory_service.messages.SuccessMessages;
import com.apnaStore.inventory_service.services.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    //    POST /api/inventories add/update
    @PostMapping
    public ResponseEntity<ApiResponse> createInventory(@RequestBody InventoryRequest inventoryRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse(Boolean.FALSE,
                        inventoryService.createInventory(inventoryRequest),
                        SuccessMessages.INVENTORY_CREATED_SUCCESSFULLY));
    }

    //    GET /api/inventories/{inventoryId}
    @GetMapping("/{inventoryId}")
    public ResponseEntity<ApiResponse> getInventoryById(@PathVariable("inventoryId") String inventoryId) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse(Boolean.FALSE,
                        inventoryService.getInventoryById(inventoryId),
                        SuccessMessages.DATA_FETCHED_SUCCESSFULLY));
    }

    //    GET /api/inventories (all)
    @GetMapping
    public ResponseEntity<ApiResponse> getAllInventory(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam(defaultValue = "name") String sortBy,
                                                       @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse(Boolean.FALSE,
                        inventoryService.getAllInventory(page, size, sortBy, sortDir),
                        SuccessMessages.DATA_FETCHED_SUCCESSFULLY));
    }

    //    GET /api/inventories/product/{productId} get inventory by product id
    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse> getInventoryByProdId(@PathVariable("productId") Long productId) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse(Boolean.FALSE,
                        inventoryService.getInventoryByProdId(productId),
                        SuccessMessages.DATA_FETCHED_SUCCESSFULLY));
    }

    //    GET /api/inventories/product/{productId}/total-stock (get total stock for a product)
    @GetMapping("/product/{productId}/total-stock")
    public ResponseEntity<ApiResponse> getTotalStock(@PathVariable("productId") Long productId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(Boolean.FALSE,
                        inventoryService.getTotalStock(productId),
                        SuccessMessages.DATA_FETCHED_SUCCESSFULLY));
    }

    //    POST /api/inventories/decrease (decrease stock (order placed))
    @PutMapping("/decrease")
    public ResponseEntity<ApiResponse> decreaseStock(@RequestBody StockUpdateRequest stockUpdateRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(Boolean.FALSE,
                        inventoryService.decreaseStock(stockUpdateRequest),
                        SuccessMessages.STOCK_DECREASED));
    }

    //    POST /api/inventories/increase (increase stock (restock or order cancel))
    @PutMapping("/increase")
    public ResponseEntity<ApiResponse> increaseStock(@RequestBody StockUpdateRequest stockUpdateRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(Boolean.FALSE,
                        inventoryService.increaseStock(stockUpdateRequest),
                        SuccessMessages.STOCK_DECREASED));
    }

    //    DELETE /api/inventories/{inventoryId}  (delete inventory record)
    @DeleteMapping("/{inventoryId}")
    public ResponseEntity<ApiResponse> deleteInventory(@PathVariable("inventoryId") String inventoryId) {
        inventoryService.removeInventory(inventoryId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(Boolean.FALSE,
                        null,
                        SuccessMessages.STOCK_DECREASED));
    }

}
