package com.apna_store.cart_service.repository;

import com.apna_store.cart_service.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
