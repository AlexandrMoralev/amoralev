package ru.job4j.servlet;

import ru.job4j.crudservlet.Store;

import java.util.Collection;
import java.util.Optional;

/**
 * Validation interface
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface Validation<E> extends Store<E> {

    Optional<Integer> add(E item);

    boolean update(E updatedItem);

    void delete(int id);

    Collection<E> findAll();

    Optional<E> findById(int id);

    Optional<E> findByLogin(String login);

    Collection<E> findByCountry(String country);

    Collection<E> findByCity(String city);

    boolean isCredential(String login, String password);

}