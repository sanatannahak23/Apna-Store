package service;

import models.Role;
import models.User;

import java.util.List;

public class Authentication {

    public static void registerUser(String userName, String email, String password, Integer role) {
        if (!OnlineFoodOrder.user
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .toList()
                .isEmpty()) throw new RuntimeException("User Already Exist With Given Email...");
        User user = new User(userName, email, password, Role.getRole(role));
        OnlineFoodOrder.user.add(user);
    }

    public static User isAuthenticated(String userName, String password) {
        List<User> list = OnlineFoodOrder.user
                .stream()
                .filter(user -> user.getEmail().equals(userName) && user.getPassword().equals(password))
                .toList();
        return list.isEmpty() ? null : list.getFirst();
    }
}
