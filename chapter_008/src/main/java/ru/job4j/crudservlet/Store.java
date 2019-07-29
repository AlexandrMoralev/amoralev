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
public interface Store<E> {

    Optional<Integer> add(E e);

    boolean update(int id, E e);

    void delete(int id);

    Collection<E> findAll();

    Optional<E> findById(int id);

    Optional<E> findByLogin(String login);

}