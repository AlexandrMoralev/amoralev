package ru.job4j.persistence;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    T save(T e);

    void update(T e);

    Optional<T> findById(Integer id);

    List<T> findByCriteria(String fieldName, String fieldValue);

    void delete(Integer id);

    List<T> findAll();

}
