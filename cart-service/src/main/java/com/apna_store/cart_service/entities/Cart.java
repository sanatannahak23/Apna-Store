package com.apna_store.cart_service.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "user_carts")
public class Cart extends Auditable{
    @Id
    private Long cartId;

    private String cartRef;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    private BigDecimal totalPrice;

    @PrePersist
    private void setCartRef() {
        this.cartRef = "cart_" + UUID.randomUUID();
    }
}
