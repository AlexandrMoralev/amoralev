package ru.job4j.servlet;

import java.util.Collection;
import java.util.Optional;

/**
 * Validation interface
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface Validation<E> {

    Optional<Integer> add(E item);

    boolean update(E updatedItem);

    boolean delete(int id);

    Collection<E> findAll();

    Optional<E> findById(int id);

    Optional<E> findByLogin(String login);

    boolean isCredential(String login, String password);

}