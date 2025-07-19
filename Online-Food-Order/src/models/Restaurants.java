package models;

import java.util.Map;
import java.util.UUID;

public class Restaurants {

    private String id;

    private String name;

    private Map<String, Double> items;

    @Override
    public String toString() {
        return "Resturant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", items=" + items +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Double> getItems() {
        return items;
    }

    public void setItems(Map<String, Double> items) {
        this.items = items;
    }

    public Restaurants(String name, Map<String, Double> items) {
        this.id = "rest_" + UUID.randomUUID();
        this.name = name;
        this.items = items;
    }
}
