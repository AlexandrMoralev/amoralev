package ru.job4j.mapping.modelsrelations.persistence.dao;

import java.util.List;
import java.util.Optional;

/**
 * Dao
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface Dao<T> {

    void save(T e);

    Optional<T> getById(Long id);

    List<T> findAll();

    void update(T e);

    void delete(T e);

}
