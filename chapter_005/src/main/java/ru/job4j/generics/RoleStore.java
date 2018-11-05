package ru.job4j.generics;

/**
 * RoleStore
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class RoleStore<T extends Role> extends AbstractStore<T> {

    public RoleStore(int size) {
        super(size);
    }
}
