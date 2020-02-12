package ru.job4j.tracker;

import java.util.Collection;
import java.util.Optional;

/**
 * ITracker
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface ITracker {

    Integer add(Item item);

    void update(Item item);

    void delete(Integer id);

    Collection<Item> findAll();

    Collection<Item> findByName(String key);

    Optional<Item> findById(Integer id);
}
