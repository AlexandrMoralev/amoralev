package ru.job4j.tracker;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Tracker
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Tracker implements ITracker {

    private final Map<String, Item> items = new HashMap<>(64);
    private static final Random RANDOM = new Random();

    /**
     * Method add - adding Item to the Tracker
     *
     * @param item Item
     * @return Item, if item added; null if the Tracker overflowed
     */
    public String add(Item item) {
        String id = this.generateId();
        this.items.put(id, Item.newBuilder().of(item).setId(id).build());
        return id;
    }

    /**
     * Method update - replaces the Item to another Item by id
     *
     * @param item - new Item for replacement
     */
    public void update(Item item) {
        this.items.replace(item.getId(), item);
    }

    /**
     * Method delete - deleting Item from the Tracker by the Id
     *
     * @param id String id of deletable Item
     */
    public void delete(String id) {
        this.items.remove(id);
    }

    /**
     * Method findAll - find all non-null Items in the Tracker
     *
     * @return Collection<Item>, empty if the Tracker without any Items
     */
    public Collection<Item> findAll() {
        return this.items.values();
    }

    /**
     * Method findByName - find all Items by the name
     *
     * @param name String name of Item
     * @return Collection<Item>, empty if there is no Items with name, or if the Tracker is empty
     */
    public Collection<Item> findByName(String name) {
        return this.items.values().stream().filter(item -> item.getName().equals(name)).collect(Collectors.toList());
    }

    /**
     * Method findById - find Item by the Id
     *
     * @param id String id of the searched element
     * @return Item by id; Item with empty fields, if the id does not belong to any item
     */
    public Optional<Item> findById(String id) {
        return Optional.ofNullable(this.items.get(id));
    }

    /**
     * Method generateId - Id generation for adding Item to the Tracker
     *
     * @return String id
     */
    private String generateId() {
        return String.valueOf(RANDOM.nextInt() + System.currentTimeMillis());
    }
}
