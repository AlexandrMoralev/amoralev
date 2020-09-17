package ru.job4j.domain;

import java.util.stream.Stream;

/**
 * Role
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public enum Role {

    ADMIN("ADMIN"),
    USER("USER"),
    GUEST("GUEST");

    private final String description;

    private Role(String description) {
        this.description = description.toUpperCase();
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return this.description;
    }

    public static Role of(String code) {
        return Stream.of(Role.values())
                .filter(v -> code.equalsIgnoreCase(v.description))
                .findFirst()
                .orElse(GUEST);
    }

}
