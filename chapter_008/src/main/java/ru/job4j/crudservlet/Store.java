package ru.job4j.crudservlet;

import java.util.Collection;
import java.util.Optional;

/**
 * Store
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface Store {

    boolean add(User user);

    boolean update(User user);

    void delete(long userId);

    Collection<User> findAll();

    Optional<User> findById(long userId);
}
