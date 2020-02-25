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

    Collection<String> addAll(Collection<E> e);

    boolean update(E e);

    int delete(String id);

    int deleteAll();

    int deleteByPeriod(LocalDateTime fromDate, LocalDateTime toDate);

    Optional<E> findById(String id);

    Optional<E> findByName(String name);

    Collection<E> findAll();

    Collection<E> findByPeriod(LocalDateTime fromDate, LocalDateTime toDate);

    Collection<E> findRecent(int count);

}