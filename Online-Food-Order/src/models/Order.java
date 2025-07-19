package models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {

    private String id;

    private String userEmail;

    private String resturantName;

    private List<String> items = new ArrayList<>();

    private Double price;

    private Long orderDate;

    private Status status;

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", resturantName='" + resturantName + '\'' +
                ", items=" + items +
                ", price=" + price +
                ", orderDate=" + orderDate +
                ", status=" + status +
                '}';
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getResturantName() {
        return resturantName;
    }

    public void setResturantName(String resturantName) {
        this.resturantName = resturantName;
    }

    public Long getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Long orderDate) {
        this.orderDate = orderDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Order(String userEmail, String resturantName, Long orderDate, Status status) {
        this.id = "Order_" + UUID.randomUUID();
        this.userEmail = userEmail;
        this.resturantName = resturantName;
        this.orderDate = orderDate;
        this.status = status;
    }
}
