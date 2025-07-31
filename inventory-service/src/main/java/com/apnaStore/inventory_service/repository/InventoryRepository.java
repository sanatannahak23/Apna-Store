package com.apnaStore.inventory_service.repository;

import com.apnaStore.inventory_service.entities.Inventory;
import com.apnaStore.inventory_service.entities.Warehouse;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends MongoRepository<Inventory, String> {

    Optional<Inventory> findByProductIdAndWarehouse(Long productId, Warehouse warehouse);

    List<Inventory> findByProductId(Long productId);

}
