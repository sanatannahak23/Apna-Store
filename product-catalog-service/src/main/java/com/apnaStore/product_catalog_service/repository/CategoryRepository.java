package com.apnaStore.product_catalog_service.repository;

import com.apnaStore.product_catalog_service.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);
}
