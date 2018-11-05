package ru.job4j.generics;

/**
 * Base
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public abstract class Base {
    private final String id;

    Base(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
