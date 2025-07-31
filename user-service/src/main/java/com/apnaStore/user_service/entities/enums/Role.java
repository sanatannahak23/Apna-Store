package com.apnaStore.user_service.entities.enums;

public enum Role {
    CUSTOMER(1,"customer"),
    SUPPORT(2,"support"),
    ADMIN(3,"admin");

    private final Integer value;
    private final String role;

    Role(Integer value, String role) {
        this.value = value;
        this.role = role;
    }

    public Integer getValue() {
        return value;
    }

    public String getRole() {
        return role;
    }

    public static Role getByValue(Integer value) {
        for (Role role : Role.values()) {
            if (role.getValue().equals(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("No role found for value: " + value);
    }
}
