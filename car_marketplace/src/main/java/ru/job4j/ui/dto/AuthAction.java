package ru.job4j.ui.dto;

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
    SIGNUP("signup"),
    UNKNOWN("unknown");

    private final String name;

    AuthAction(String description) {
        this.name = description;
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
                .filter(v -> v.name.equalsIgnoreCase(name))
                .findFirst()
                .orElse(UNKNOWN);
    }

}
