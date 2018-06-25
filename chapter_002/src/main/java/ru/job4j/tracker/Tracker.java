package ru.job4j.tracker;

import java.util.ArrayList;
import java.util.Random;

/**
 * Tracker
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Tracker {

    private final ArrayList<Item> items = new ArrayList<>();
    private static final Random RANDOM = new Random();

    /**
     * Method add - adding Item to the Tracker
     * @param item Item
     * @return Item, if item added; null if the Tracker overflowed
     */
    public Item add(Item item) {
        item.setId(this.generateId());
        this.items.add(item);
        return item;
    }

    /**
     * Method generateId - Id generation for adding Item to the Tracker
     * @return String id
     */
    private String generateId() {
        return String.valueOf(RANDOM.nextInt() + System.currentTimeMillis());
    }

    /**
     * Method replace - replaces the Item to another Item by id
     * @param id - String id of Item to replace
     * @param anItem - new Item for replacement
     */
    public void replace(String id, Item anItem) {
        for (Item item : items) {
            if (item.getId().equals(id)) {
                anItem.setId(item.getId());
                items.set(items.indexOf(item), anItem);
                break;
            }
        }
    }

    /**
     * Method delete - deleting Item from the Tracker by the Id
     * @param id String id of deletable Item
     */
    public void delete(String id) {
        for (Item item : items) {
            if (id.equals(item.getId())) {
                items.remove(item);
                break;
            }
        }
    }

    /**
     * Method findAll - find all non-null Items in the Tracker
     * @return ArrayList<Item>, empty if the Tracker without any Items
     */
    public ArrayList<Item> findAll() {
        return this.items;
    }

    /**
     * Method findByName - find all Items by the name
     * @param key String name of Item
     * @return ArrayList<Item>, empty if there is no Items with name = key, or if the Tracker is empty
     */
    public ArrayList<Item> findByName(String key) {
        ArrayList<Item> result = new ArrayList<>();
        for (Item item : this.items) {
            if (key.equals(item.getName())) {
                result.add(item);
            }
        }
        return result;
    }

    /**
     * Method findById - find Item by the Id
     * @param id String id of the searched element
     * @return Item by id; null if the id does not belong to any item
     */
    public Item findById(String id) {
        Item result = null;
        for (Item item : this.items) {
            if (id.equals(item.getId())) {
                result = item;
                break;
            }
        }
        return result;
    }
}
