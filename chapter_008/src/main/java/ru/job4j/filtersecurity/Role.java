package ru.job4j.filtersecurity;

public enum  Role {
    ROOT(0, "root"),
    ADMIN(1, "admin"),
    MANAGER(2, "manager"),
    USER(3, "user"),
    MAINTENANCE(4, "maintenance"),
    DEFAULT(5, "guest");

    private int index;
    private String description;

    private Role(int index, String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getIndex() {
        return index;
    }
}
