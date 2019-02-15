package ru.job4j.tracker;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Tracker
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Tracker implements ITracker {

    private final List<Item> items = new ArrayList<>();
    private static final Random RANDOM = new Random();

    /**
     * Method add - adding Item to the Tracker
     *
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
     *
     * @return String id
     */
    private String generateId() {
        return String.valueOf(RANDOM.nextInt() + System.currentTimeMillis());
    }

    /**
     * Method replace - replaces the Item to another Item by id
     *
     * @param id     - String id of Item to replace
     * @param anItem - new Item for replacement
     */
    public void replace(String id, Item anItem) {
        Predicate<Item> itemPredicate = item -> Objects.equals(id, item.getId());
        Consumer<Item> replaceItem = item -> {
            anItem.setId(item.getId());
            items.set(items.indexOf(item), anItem);
        };
        Stream.of(this.items)
                .flatMap(itemList -> itemList.stream().filter(itemPredicate))
                .forEach(replaceItem);
    }

    /**
     * Method delete - deleting Item from the Tracker by the Id
     *
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
     *
     * @return List<Item>, empty if the Tracker without any Items
     */
    public List<Item> findAll() {
        return this.items;
    }

    /**
     * Method findByName - find all Items by the name
     *
     * @param key String name of Item
     * @return List<Item>, empty if there is no Items with name = key, or if the Tracker is empty
     */
    public List<Item> findByName(String key) {
        Predicate<Item> isNameMatched = item -> key.equals(item.getName());
        Function<List<Item>, Stream<Item>> listMapper = itemList -> itemList.stream().filter(isNameMatched);

        return Stream.of(this.items)
                .flatMap(listMapper)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Method findById - find Item by the Id
     *
     * @param id String id of the searched element
     * @return Item by id; Item with empty fields, if the id does not belong to any item
     */
    public Item findById(String id) {
        Item stub = new Item("", "");
        Predicate<Item> isIdMatched = item -> Objects.equals(item.getId(), id);
        return this.items.stream().filter(Objects::nonNull).filter(isIdMatched).findFirst().orElse(stub);
    }
}
