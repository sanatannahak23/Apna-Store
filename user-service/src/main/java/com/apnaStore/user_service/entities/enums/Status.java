package com.apnaStore.user_service.entities.enums;

public enum Status {
    ACTIVE(1,"active"),
    INACTIVE(2,"inactive"),
    BLOCKED(3,"blocked");

    private final Integer value;
    private final String status;

    Status(Integer value, String status) {
        this.value = value;
        this.status = status;
    }

    public Integer getValue() {
        return value;
    }

    public String getStatus() {
        return status;
    }

    public static Status getByValue(Integer value) {
        for (Status status : Status.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No Status found for value: " + value);
    }
}
