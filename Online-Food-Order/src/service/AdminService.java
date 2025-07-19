package service;

import models.Order;
import models.Restaurants;
import models.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AdminService {

    public static void adminPage(User user) {
        System.out.println("-------------------------- Admin page --------------------------");
        Scanner scanner = new Scanner(System.in);
        int ch = 0;

        while (ch != 8) {
            System.out.println("1. Get All Users");
            System.out.println("2. Get All Restaurants");
            System.out.println("3. Get All Orders");
            System.out.println("4. Add New Restaurants");
            System.out.println("5. Remove Restaurants");
            System.out.println("6. Remove User");
            System.out.println("7. Add User");
            System.out.println("8. Exit");
            System.out.print("Enter Your Choice :: ");

            if (scanner.hasNextInt()) {
                ch = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("Please enter a valid number.");
                scanner.nextLine();
                continue;
            }

            switch (ch) {
                case 1 -> {
                    for (User u : getAllUser()) {
                        System.out.println(u);
                    }
                }

                case 2 -> {
                    for (Restaurants rest : getAllRestaurant()) {
                        System.out.println(rest);
                    }
                }

                case 3 -> {
                    for (Order order : getAllOrders()) {
                        System.out.println(order);
                    }
                }

                case 4 -> {
                    System.out.print("Enter Restaurant name :: ");
                    String name = scanner.nextLine();

                    System.out.print("How Many Item You Want To Add In Your Menu :: ");
                    int num = scanner.nextInt();
                    scanner.nextLine();

                    HashMap<String, Double> items = new HashMap<>();
                    for (int i = 0; i < num; i++) {
                        System.out.print("Enter Item name :: ");
                        String item = scanner.nextLine();
                        System.out.print("Enter Item price :: ");
                        Double price = scanner.nextDouble();
                        scanner.nextLine();
                        items.put(item, price);
                    }

                    addRestaurant(name, items);
                }

                case 5 -> {
                    System.out.print("Enter Restaurant name :: ");
                    String name = scanner.nextLine();
                    if (removeRestaurant(name))
                        System.out.println("Restaurant removed successfully.");
                    else
                        System.out.println("Something went wrong...");
                }

                case 6 -> {
                    System.out.print("Enter user emailId :: ");
                    String email = scanner.nextLine();
                    if (removeUser(email))
                        System.out.println("User removed successfully.");
                    else
                        System.out.println("Something went wrong...");
                }

                case 7 -> {
                    System.out.print("Enter name :: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter email :: ");
                    String email = scanner.nextLine();

                    System.out.print("Enter password :: ");
                    String password = scanner.nextLine();

                    System.out.print("Select the role :: \n1. User\n2. Admin\nEnter your choice :: ");
                    int role = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    try {
                        Authentication.registerUser(name, email, password, role);
                    } catch (RuntimeException ex) {
                        System.out.println(ex.getMessage());
                    }
                }

                case 8 -> System.out.println("Exiting Admin Panel...");

                default -> System.out.println("Invalid choice...");
            }
        }

    }

    public static List<User> getAllUser() {
        return OnlineFoodOrder.user;
    }

    public static List<Restaurants> getAllRestaurant() {
        if (OnlineFoodOrder.restaurants.isEmpty()) {
            System.out.println("There is no restaurant present yet...");
            return List.of();
        }
        return OnlineFoodOrder.restaurants;
    }

    public static List<Order> getAllOrders() {
        if (OnlineFoodOrder.order.isEmpty()) {
            System.out.println("There is no order created yet...");
            return List.of();
        }
        return OnlineFoodOrder.order;
    }

    public static Boolean addRestaurant(String name, Map<String, Double> items) {
        OnlineFoodOrder.restaurants.add(new Restaurants(name, items));
        return Boolean.TRUE;
    }

    public static Boolean removeRestaurant(String name) {
        List<Restaurants> restaurants = OnlineFoodOrder.restaurants;
        OnlineFoodOrder.restaurants = restaurants
                .stream()
                .filter(rest -> !rest.getName().equalsIgnoreCase(name))
                .toList();
        return Boolean.TRUE;
    }

    public static Boolean removeUser(String email) {
        OnlineFoodOrder.user = OnlineFoodOrder.user
                .stream()
                .filter(user -> !user.getEmail().equalsIgnoreCase(email))
                .toList();
        return Boolean.TRUE;
    }
}
