package com.apna_store.common_service.entities.enums;

public enum ReferenceType {
    PRODUCT(1, "product"),
    CATEGORY(2, "category"),
    PROFILE(3, "profile"),
    OTHERS(4, "others");

    private Integer value;
    private String type;

    ReferenceType(Integer value, String type) {
        this.value = value;
        this.type = type;
    }

    public static ReferenceType type(String type) {
        for (ReferenceType ref : values()) {
            if (ref.type.equalsIgnoreCase(type)) return ref;
        }
        return null;
    }
}