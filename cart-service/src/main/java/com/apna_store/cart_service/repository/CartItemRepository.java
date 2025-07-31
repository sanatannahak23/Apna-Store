package com.apna_store.cart_service.repository;

import com.apna_store.cart_service.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
