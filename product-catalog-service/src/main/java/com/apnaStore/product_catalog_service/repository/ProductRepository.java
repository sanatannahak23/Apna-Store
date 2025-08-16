package com.apnaStore.product_catalog_service.repository;

import com.apnaStore.product_catalog_service.entities.Category;
import com.apnaStore.product_catalog_service.entities.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);

    List<Product> findByCategory(Category category);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(p.category.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Product> searchByNameAndCategory(Pageable pageable, @Param("search") String search);
}
