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

    String add(Item item);

    void update(Item item);

    void delete(String id);

    Collection<Item> findAll();

    Collection<Item> findByName(String key);

    Optional<Item> findById(String id);
}
