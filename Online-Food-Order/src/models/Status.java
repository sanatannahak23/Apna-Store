package models;

public enum Status {
    BOOKED(1, "Booked"),
    PROCCESS(2, "Procces"),
    DELIVERED(3, "Delivered");

    private Integer value;
    private String status;

    Status(Integer value, String status) {
        this.value = value;
        this.status = status;
    }
}
