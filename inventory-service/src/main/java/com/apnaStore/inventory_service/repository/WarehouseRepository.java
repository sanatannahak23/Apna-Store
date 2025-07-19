package com.apnaStore.inventory_service.repository;

import com.apnaStore.inventory_service.entities.Warehouse;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WarehouseRepository extends MongoRepository<Warehouse, String> {
    Optional<Warehouse> findByName(String name);
}
