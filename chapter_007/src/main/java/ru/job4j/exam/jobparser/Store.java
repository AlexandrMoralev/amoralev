package ru.job4j.exam.jobparser;

import org.joda.time.LocalDateTime;

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

    Optional<String> add(E e);

    void addAll(Collection<E> e);

    boolean update(E e);

    boolean delete(String id);

    void deleteAll();

    int deleteByPeriod(LocalDateTime fromDate, LocalDateTime toDate);

    Optional<E> findById(String id);

    Collection<E> findByName(String name);

    Collection<E> findAll();

    Collection<E> findByPeriod(LocalDateTime fromDate, LocalDateTime toDate);

    Collection<E> findRecent(int count);

}