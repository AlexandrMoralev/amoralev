package ru.job4j.ui.model;

import java.util.stream.Stream;

/**
 * AuthAction
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public enum AuthAction {

    SIGNIN("signin"),
    REGISTER("register"),
    LOGOUT("logout"),
    DEFAULT("default");

    private final String name;

    AuthAction(String description) {
        this.name = description.toLowerCase();
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static AuthAction of(String name) {
        return Stream.of(AuthAction.values())
                .filter(v -> name.equalsIgnoreCase(v.name))
                .findFirst()
                .orElse(DEFAULT);
    }

}
