package ru.job4j.tracker;

import java.util.List;

/**
 * ITracker
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface ITracker {
    Item add(Item item);
    void replace(String id, Item item);
    void delete(String id);
    List<Item> findAll();
    List<Item> findByName(String key);
    Item findById(String id);
}
