package com.apna_store.order_service.entities.enums;

public enum OrderStatus {
    PENDING(1, "pending"), CONFIRMED(2, "confirmed"), SHIPPED(3, "shipped"),
    DELIVERED(4, "delivered"), CANCELLED(5, "cancelled");

    private Integer value;
    private String status;

    OrderStatus(Integer value, String status) {
        this.value = value;
        this.status = status;

    }

    public static OrderStatus getStatus(String status) {
        for (OrderStatus orderStatus : values()) {
            if (orderStatus.status.equalsIgnoreCase(status)) return orderStatus;
        }
        throw new RuntimeException("Invalid given status");
    }
}