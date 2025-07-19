package com.apnaStore.product_catalog_service.repository;

import com.apnaStore.product_catalog_service.entities.Product;
import com.apnaStore.product_catalog_service.entities.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, Long> {

    List<ProductAttribute> findByProduct(Product product);
}
