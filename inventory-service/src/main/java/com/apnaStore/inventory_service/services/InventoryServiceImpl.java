package com.apnaStore.inventory_service.services;

import com.apnaStore.inventory_service.clients.ProductClient;
import com.apnaStore.inventory_service.dto.request.InventoryRequest;
import com.apnaStore.inventory_service.dto.request.StockUpdateRequest;
import com.apnaStore.inventory_service.dto.response.ApiResponse;
import com.apnaStore.inventory_service.dto.response.InventoryResponse;
import com.apnaStore.inventory_service.entities.Inventory;
import com.apnaStore.inventory_service.entities.Warehouse;
import com.apnaStore.inventory_service.exception.DataAlreadyExist;
import com.apnaStore.inventory_service.exception.DataNotFoundException;
import com.apnaStore.inventory_service.exception.InsufficientStock;
import com.apnaStore.inventory_service.helper.EntityToResponse;
import com.apnaStore.inventory_service.messages.ExceptionMessages;
import com.apnaStore.inventory_service.repository.InventoryRepository;
import com.apnaStore.inventory_service.repository.WarehouseRepository;
import com.apnaStore.inventory_service.services.service.InventoryService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    private final WarehouseRepository warehouseRepository;

    private final ProductClient productClient;

    @Override
    @CircuitBreaker(name = "productClient", fallbackMethod = "getProductFallback")
    public InventoryResponse createInventory(InventoryRequest inventoryRequest) {
        // To check the product we need to use feignClient to call product-catalog-service
        ApiResponse response = productClient.getProductById(inventoryRequest.getProductId());
        log.info("Product Detail :: {}", response);
        Warehouse warehouse = warehouseRepository.findById(inventoryRequest.getWarehouseId())
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND));

        inventoryRepository.findByProductIdAndWarehouse(inventoryRequest.getProductId(), warehouse)
                .ifPresent(ex -> {
                    throw new DataAlreadyExist(ExceptionMessages.INVENTORY_ALREADY_PRESENT);
                });
        Inventory inventory = new Inventory();
        inventory.setWarehouse(warehouse);
        inventory.setStock(inventoryRequest.getStock());
        inventory.setProductId(inventoryRequest.getProductId());
        inventory = inventoryRepository.save(inventory);
        return EntityToResponse.inventoryToResponse(inventory);
    }

    public ApiResponse getProductFallback(Long productId, Throwable t) {
        return new ApiResponse(true, null, "Fallback: " + t.getMessage());
    }

    @Override
    public InventoryResponse getInventoryById(String inventoryId) {
        log.info("Inventory Id :: {}", inventoryId);
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND));
        log.info("inventory :: {}", inventory);
        return EntityToResponse.inventoryToResponse(inventory);
    }

    @Override
    @CircuitBreaker(name = "productClient", fallbackMethod = "getProductFallback")
    public List<InventoryResponse> getInventoryByProdId(Long productId) {
        ApiResponse product = productClient.getProductById(productId);

        log.info("Product Detail :: {}", product);
        List<Inventory> inventories = inventoryRepository.findByProductId(productId);
        return inventories
                .stream()
                .map(EntityToResponse::inventoryToResponse)
                .toList();
    }

    @Override
    @CircuitBreaker(name = "productClient", fallbackMethod = "getProductFallback")
    public Long getTotalStock(Long productId) {
        ApiResponse product = productClient.getProductById(productId);
        log.info("Product Detail :: {}", product);
        List<Inventory> inventories = inventoryRepository.findByProductId(productId);
        return inventories
                .stream()
                .map(Inventory::getStock)
                .mapToLong(Long::valueOf)
                .sum();
    }

    @Override
    public InventoryResponse decreaseStock(StockUpdateRequest stockUpdateRequest) {
        Inventory inventory = inventoryRepository.findById(stockUpdateRequest.getInventoryId())
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND));

        if (inventory.getStock() < stockUpdateRequest.getQuantity())
            throw new InsufficientStock(ExceptionMessages.INSUFFICIENT_STOCK);

        inventory.setStock(inventory.getStock() - stockUpdateRequest.getQuantity());
        inventory = inventoryRepository.save(inventory);
        return EntityToResponse.inventoryToResponse(inventory);
    }

    @Override
    public InventoryResponse increaseStock(StockUpdateRequest stockUpdateRequest) {
        Inventory inventory = inventoryRepository.findById(stockUpdateRequest.getInventoryId())
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND));

        inventory.setStock(inventory.getStock() + stockUpdateRequest.getQuantity());
        inventory = inventoryRepository.save(inventory);
        return EntityToResponse.inventoryToResponse(inventory);
    }

    @Override
    public void removeInventory(String inventoryId) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND));
        inventoryRepository.delete(inventory);
    }

    @Override
    public List<InventoryResponse> getAllInventory(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return inventoryRepository.findAll(pageable).getContent()
                .stream()
                .map(EntityToResponse::inventoryToResponse)
                .toList();
    }
}
