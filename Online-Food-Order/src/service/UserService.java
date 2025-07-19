package service;

import models.Order;
import models.Restaurants;
import models.Status;
import models.User;

import java.util.*;

public class UserService {

    public static void userPage(User user) {
        System.out.println("-------------------------- User page --------------------------");
        Scanner scanner = new Scanner(System.in);
        int ch = 0;
        while (ch != 3) {
            System.out.println("1. Book An Order");
            System.out.println("2. Get All Order");
            System.out.println("3. Exit");
            System.out.print("Enter Your Choice :: ");
            ch = scanner.nextInt();
            switch (ch) {
                case 1 -> {
                    int i = 0;
                    for (Restaurants restaurants : OnlineFoodOrder.restaurants) {
                        System.out.println(++i + ". " + restaurants.getName());
                    }
                    System.out.println("Select Restarunt :: ");
                    int rest = scanner.nextInt();
                    try {
                        bookOrder(rest, user);
                    } catch (RuntimeException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                case 2 -> {
                    for (Order order : getAllOrder(user)) {
                        System.out.println(order);
                    }
                }
                default -> System.out.println("Invalid choice..");
            }
        }
    }

    public static void bookOrder(Integer resturant, User user) {
        if (OnlineFoodOrder.restaurants.size() < resturant) throw new RuntimeException("Invalid restaurant .....");
        Scanner scanner = new Scanner(System.in);
        Restaurants restaurants = OnlineFoodOrder.restaurants.get(resturant - 1);
        System.out.println("------------------------------- Restaurant Menu ----------------------------");
        Set<Map.Entry<String, Double>> entries = restaurants.getItems().entrySet();
        int i = 0;
        for (Map.Entry entry : entries) {
            System.out.println(++i + ". " + entry.getKey() + " -------------------------------------- " + entry.getValue());
        }
        System.out.println("Select your food :: ");
        int ch = scanner.nextInt();

        Order order = new Order(user.getEmail(), restaurants.getName(), System.currentTimeMillis(), Status.BOOKED);
        Iterator<Map.Entry<String, Double>> iterator = entries.iterator();
        int j = 1;
        while (iterator.hasNext()) {
            Map.Entry<String, Double> entry = iterator.next();
            if (j == ch) {
                order.getItems().add(entry.getKey());
                order.setPrice(entry.getValue());
                break;
            }
            j++;
        }

        OnlineFoodOrder.order.add(order);
    }

    public static List<Order> getAllOrder(User user) {
        return OnlineFoodOrder
                .order
                .stream()
                .filter(order -> order.getUserEmail().equals(user.getEmail()))
                .toList();
    }
}
