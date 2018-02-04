package ru.job4j.tracker;


import org.junit.Test;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * TrackerTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class TrackerTest {
    /**
     * Test. Adding new Item to the Tracker
     */
    @Test
    public void whenAddNewItemThenTrackerHasTheSameItem() {
        Tracker tracker = new Tracker();
        Item item = new Item("testItem", "testItemDescription", 1L);

        tracker.add(item);
        assertThat(tracker.findAll()[0].getName(), is(item.getName()));
    }

    /**
     * Test. Replacing Item in the Tracker
     */
    @Test
    public void whenReplacingItemThenTrackerHasNewItemInsteadReplaced() {
        Tracker tracker = new Tracker();

        tracker.add(new Item("A", "Description of A", 1L));
        tracker.add(new Item("C", "Description of C", 3L));

        Item replaceableItem = tracker.add(new Item("B", "Description of B", 2L));

        Item itemForReplacement = new Item("new item for replacement", "new description", 99L);

        tracker.replace(replaceableItem.getId(), itemForReplacement);

        assertThat(tracker.findById(replaceableItem.getId()).getName(), is("new item for replacement"));
    }

    /**
     * Test. Deleting Item in the Tracker
     */
    @Test
    public void whenDeleteItemThenTrackerWithoutDeletedItem() {
        Tracker tracker = new Tracker();

        Item deleteableItem = new Item("D", "Del", 1L);

        tracker.add(deleteableItem);
        Item item = tracker.add(new Item("nonDeleteableItem", "NonDel", 2L));

        tracker.delete(deleteableItem.getId());

        assertThat((tracker.findAll()[0].getId()), is(item.getId()));
    }

    /**
     * Test. Find all Items in the Tracker
     */
    @Test
    public void whenFindAllThenGetTrackerWithoutNullItems() {
        Tracker firstTracker = new Tracker();
        Tracker secondTracker = new Tracker();

        Item firstItem = firstTracker.add(new Item("A", "Description of A", 1L));
        Item secondItem = firstTracker.add(new Item("B", "Description of B", 2L));

        Item sharedItem = new Item("C", "Description of C", 3L);

        firstTracker.add(sharedItem);
        secondTracker.add(sharedItem);


        firstTracker.delete(firstItem.getId());
        firstTracker.delete(secondItem.getId());

        assertThat(firstTracker.findAll()[0].getName(), is(secondTracker.findAll()[0].getName()));
    }

    /**
     * Test. Searching Item by the name
     */
    @Test
    public void whenFindItemByNameThenItemsWithThatName() {
        Tracker tracker = new Tracker();

        tracker.add(new Item("A", "Description of A0", 1L));
        tracker.add(new Item("B", "Description of B", 2L));
        tracker.add(new Item("C", "Description of C0", 3L));
        tracker.add(new Item("A", "Description of A1", 15L));
        tracker.add(new Item("C", "Description of C1", 31L));
        tracker.add(new Item("A", "Description of A2", 151L));

        Item[] result = tracker.findByName("A");

        boolean resultIs = false;

        for (Item item : result) {
            resultIs = item.getName().equals("A") | resultIs;
        }

        assertThat(resultIs, is(true));
    }

    /**
     * Test. Searching Item by the Id
     */
    @Test
    public void whenFindByIdThenItemWithThatId() {
        Tracker tracker = new Tracker();

        tracker.add(new Item("A", "Description of A0", 1L));
        tracker.add(new Item("B", "Description of B", 2L));
        tracker.add(new Item("C", "Description of C0", 3L));
        tracker.add(new Item("A", "Description of A1", 15L));

        Item item =  tracker.add(new Item("C", "Description of C1", 31L));
        tracker.add(new Item("A", "Description of A2", 151L));

        assertThat(tracker.findById(item.getId()).getCreated(), is(31L));

    }
}
