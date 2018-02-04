package ru.job4j.tracker;

import java.util.Random;

/**
 * Tracker
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Tracker {

    private final Item[] items = new Item[100];
    private int position = 0;
    private static final Random RANDOM = new Random();

    /**
     * Method add - adding Item to the Tracker
     * @param item Item
     * @return Item, if item added; null if the Tracker overflowed
     */
    public Item add(Item item) {
        Item result = null;

        if (position < this.items.length) {
            result = item;
            result.setId(this.generateId());
            this.items[position++] = result;
        }

        return result;
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

        for (int itemsIndex = 0; itemsIndex < this.position; itemsIndex++) {
            if (this.items[itemsIndex].getId().equals(id)) {
                anItem.setId(this.items[itemsIndex].getId());
                this.items[itemsIndex] = anItem;
                break;
            }
        }
    }

    /**
     * Method delete - deleting Item from the Tracker by the Id
     * next elements are moved one position to the beginning of this.items[]
     * @param id String id of deletable Item
     */
    public void delete(String id) {

        for (int index = 0; index < this.position; index++) {
            if (id.equals(this.items[index].getId())) {

                this.items[index] = null;
                System.arraycopy(
                        this.items,
                        index + 1,
                        this.items,
                        index,
                        this.items.length - index - 1
                );
                this.items[--position] = null;
                break;
            }
        }
    }

    /**
     * Method findAll - find all non-null Items in the Tracker
     * @return Item[]; null if the Tracker without any Items
     */
    public Item[] findAll() {
        Item[] result = null;

        if (this.position > 0) {
            result = new Item[position];
            System.arraycopy(this.items, 0, result, 0, this.position);
        }

        return result;
    }

    /**
     * Method findByName - find all Items by the name
     * @param key String name of Item
     * @return Item[]; null if there is no Items with name = key, or if the Tracker is empty
     */
    public Item[] findByName(String key) {
        Item[] result = null;
        int counter = 0;

        for (int index = 0; index < this.position; index++) {
            if (this.items[index].getName().equals(key)) counter++;
        }

        if (counter > 0) {
            result = new Item[counter];
            int searchStoppedPosition = 0;

            for (int resultIndex = 0; resultIndex < result.length; resultIndex++) {
                for (int itemsIndex = searchStoppedPosition; itemsIndex < this.position; itemsIndex++) {
                    if (this.items[itemsIndex].getName().equals(key)) {
                        result[resultIndex] = this.items[itemsIndex];
                        searchStoppedPosition = itemsIndex + 1;
                        break;
                    }
                }
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

        for (int index = 0; index < this.position; index++) {
            if (id.equals(this.items[index].getId())) {
                result = this.items[index];
                break;
            }
        }
        return result;
    }
}
