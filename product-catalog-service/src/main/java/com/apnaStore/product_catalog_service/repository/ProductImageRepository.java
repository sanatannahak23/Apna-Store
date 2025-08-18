package com.apnaStore.product_catalog_service.repository;

import com.apnaStore.product_catalog_service.entities.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}
