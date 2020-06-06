package ru.job4j.tracker;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TrackerTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class TrackerTest {
    private Tracker tracker;

    @BeforeEach
    public void init() {
        this.tracker = new Tracker();
    }

    @AfterEach
    public void cleanUp() {
        this.tracker = null;
    }

    /**
     * Test. Adding new Item to the Tracker
     */
    @Test
    public void whenAddNewItemThenTrackerHasTheSameItem() {

        Item item = new Item("testItem", "testItemDescription");

        tracker.add(item);

        Collection<Item> itemsByName = tracker.findByName("testItem");
        assertEquals(1, itemsByName.size());
        assertEquals("testItem", itemsByName.iterator().next().getName());

        assertTrue(tracker.findAll().stream()
                .allMatch(it -> "testItem".equals(it.getName())
                        && "testItemDescription".equals(it.getDescription())
                )
        );
    }

    /**
     * Test. Replacing Item in the Tracker
     */
    @Test
    public void whenReplacingItemThenTrackerHasNewItemInsteadReplaced() {

        tracker.add(new Item("A", "Description of A"));
        tracker.add(new Item("C", "Description of C"));


        Integer replaceableItemId = tracker.add(new Item("B", "Description of B"));
        Item item = tracker.findById(replaceableItemId).get();

        Item itemForReplacement = Item.newBuilder().of(item).setName("new item for replacement").setDescription("new description").build();

        tracker.update(itemForReplacement);

        assertEquals("new item for replacement", tracker.findById(replaceableItemId).get().getName());
    }

    /**
     * Test. Deleting Item in the Tracker
     */
    @Test
    public void whenDeleteItemThenTrackerWithoutDeletedItem() {

        Integer deletableItemId = tracker.add(new Item("deletableItem", "Del"));

        Integer nonDeletableItemId = tracker.add(new Item("nonDeletableItem", "NonDel"));

        tracker.delete(deletableItemId);

        assertTrue(tracker.findByName("deletableItem").isEmpty());
        assertTrue(tracker.findById(deletableItemId).isEmpty());
        assertTrue(tracker.findAll().stream().noneMatch(it -> "deletableItem".equals(it.getName())));

        assertEquals(1, tracker.findByName("nonDeletableItem").size());
        assertTrue(tracker.findById(nonDeletableItemId).isPresent());
        assertTrue(tracker.findAll().stream().allMatch(it -> "nonDeletableItem".equals(it.getName())));
    }

    /**
     * Test. Find all Items in the Tracker
     */
    @Test
    public void whenFindAllThenGetTrackerWithoutNullItems() {
        Tracker firstTracker = new Tracker();
        Tracker secondTracker = new Tracker();

        Integer firstItemId = firstTracker.add(new Item("A", "Description of A"));
        Integer secondItemId = firstTracker.add(new Item("B", "Description of B"));

        Item sharedItem = new Item("C", "Description of C");

        firstTracker.add(sharedItem);
        secondTracker.add(sharedItem);

        firstTracker.delete(firstItemId);
        firstTracker.delete(secondItemId);

        assertTrue(firstTracker.findAll().stream().noneMatch(it -> "A".equals(it.getName())));
        assertTrue(secondTracker.findAll().stream().noneMatch(it -> "B".equals(it.getName())));

        assertTrue(firstTracker.findById(firstItemId).isEmpty());
        assertTrue(secondTracker.findById(secondItemId).isEmpty());

        assertTrue(firstTracker.findAll().stream().allMatch(it -> "C".equals(it.getName()) && "Description of C".equals(it.getDescription())));
        assertTrue(secondTracker.findAll().stream().allMatch(it -> "C".equals(it.getName()) && "Description of C".equals(it.getDescription())));
    }

    /**
     * Test. Searching Item by the name
     */
    @Test
    public void whenFindItemByNameThenItemsWithThatName() {

        tracker.add(new Item("A", "Description of A0"));
        tracker.add(new Item("B", "Description of B"));
        tracker.add(new Item("C", "Description of C0"));
        tracker.add(new Item("A", "Description of A1"));
        tracker.add(new Item("C", "Description of C1"));
        tracker.add(new Item("A", "Description of A2"));

        Collection<Item> items = tracker.findByName("A");

        assertEquals(3, items.size());
        assertTrue(items.stream().allMatch(item -> "A".equals(item.getName())));
    }

    /**
     * Test. Searching Item by the Id
     */
    @Test
    public void whenFindByIdThenItemWithThatId() {

        tracker.add(new Item("A", "Description of A0"));
        tracker.add(new Item("B", "Description of B"));
        tracker.add(new Item("C", "Description of C0"));
        tracker.add(new Item("A", "Description of A1"));

        Integer itemId = tracker.add(new Item("C", "Description of C1"));
        tracker.add(new Item("A", "Description of A2"));

        assertEquals("Description of C1", tracker.findById(itemId).get().getDescription());
    }
}
