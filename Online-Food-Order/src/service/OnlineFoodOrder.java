package service;

import models.Order;
import models.Restaurants;
import models.Role;
import models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OnlineFoodOrder {

    public static List<User> user = new ArrayList<>();

    public static List<Restaurants> restaurants = new ArrayList<>();

    public static List<Order> order = new ArrayList<>();

    public static void main(String[] args) {
        user.add(new User("Sanatana Nahak", "sanatan@gmail.com", "Sanatan@2001", Role.ADMIN));
        System.out.println("------------------------------------------Wel-Come to Online Food Order-------------------------------------------");
        Scanner scanner = new Scanner(System.in);
        int n = 0;

        while (n != 3) {
            System.out.println("1. Register User\n2. Login User\n3. Exit");
            System.out.print("Select One Of The Option To Proceed Further :: ");
            n = scanner.nextInt();
            scanner.nextLine();

            switch (n) {
                case 1 -> {
                    System.out.println("---------------- Register ----------------------");
                    System.out.print("Enter name :: ");
                    String name = scanner.nextLine();  // use nextLine for full name

                    System.out.print("Enter email :: ");
                    String email = scanner.nextLine();

                    System.out.print("Enter password :: ");
                    String password = scanner.nextLine();

                    try {
                        Authentication.registerUser(name, email, password, 1);
                    } catch (RuntimeException ex) {
                        System.out.println(ex.getMessage());
                    }
                }

                case 2 -> {
                    System.out.println("------------------- Login ----------------------------");
                    System.out.print("Enter email :: ");
                    String email = scanner.nextLine();

                    System.out.print("Enter password :: ");
                    String password = scanner.nextLine();

                    User authenticated = Authentication.isAuthenticated(email, password);

                    if (authenticated != null && authenticated.getRole().equals(Role.ADMIN)) {
                        AdminService.adminPage(authenticated);
                    } else if (authenticated != null && authenticated.getRole().equals(Role.USER)) {
                        UserService.userPage(authenticated);
                    } else {
                        System.out.println("Invalid User Details...");
                    }
                }

                case 3 -> {
                    System.out.println("Exiting... Goodbye!");
                    break;
                }

                default -> System.out.println("Invalid choice....!!!!");
            }
        }

    }
}
