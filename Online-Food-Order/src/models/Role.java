package models;

public enum Role {
    USER(1, "User"), ADMIN(2, "Admin");

    private Integer value;
    private String role;

    Role(Integer value, String role) {
        this.value = value;
        this.role = role;
    }

    public static Role getRole(Integer value) {
        for (Role role : values()) {
            if (role.value.equals(value)) return role;
        }
        throw new RuntimeException("Invalid role");
    }

    public static String getRole(Role role) {
        return role.role;
    }
}
