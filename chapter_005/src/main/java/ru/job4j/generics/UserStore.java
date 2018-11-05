package ru.job4j.generics;

/**
 * UserStore
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class UserStore<T extends User> extends AbstractStore<T> {

    public UserStore(int size) {
        super(size);
    }
}
