package ru.job4j.tracker;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.StringJoiner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * StartUITest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class StartUITest {
    // std SyStem.out reference
    private final PrintStream stdOut = System.out;
    // "buffered" output for tests
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    private Tracker tracker;
    private Input input;

    private String output;
    private String expected;

    /**
     * Method setBufferedOutput - replaces stdOut to "buffered" out
     */
    @Before
    public void setBufferedOutput() {
        System.setOut(new PrintStream(out));
    }

    /**
     * Method trackerInit - creates new Tracker instance
     */
    @Before
    public void trackerInit() {
        this.tracker = new Tracker();
    }

    /**
     * Method setStdOutputBack - replaces "buffered" out back to std System.out after test
     */
    @After
    public void setStdOutputBack() {
        System.setOut(stdOut);
    }

    /**
     * Test. Adding Item to Tracker.
     */
    @Test
    public void whenUserAddItemThenTrackerHasNewItemWithSameName() {
        input = new StubInput(new String[]{"0", "test name", "desc", "", "6", "y"});
        new StartUI(input, tracker).init();
        assertEquals("test name", tracker.findAll().stream().findFirst().get().getName());
    }

    /**
     * Test. Editing Item.
     */
    @Test
    public void whenUserEditItemThenTrackerHasUpdatedValue() {
        String id = tracker.add(new Item("test name", "desc"));
        input = new StubInput(new String[]{"2", id, "new test name", "new desc", "", "6", "y"});
        new StartUI(input, tracker).init();
        assertThat(tracker.findById(id).get().getName(), is("new test name"));
    }

    /**
     * Test. Deleting Item.
     */
    @Test
    public void wheUserDeleteItemThenTrackerHasNoItemWithThatId() {
        String id = tracker.add(new Item("test name", "desc"));
        tracker.add(new Item("other name", "other desc"));
        input = new StubInput(new String[]{"3", id, "", "6", "y"});
        new StartUI(input, tracker).init();
        assertEquals("other name", tracker.findAll().stream().findFirst().get().getName());
    }

    /**
     * Test. Printing all Tracker Items.
     */
    @Test
    public void whenUserChecksAllItemsThenPrintingAllItemsInConsole() {

        String id = tracker.add(new Item("first name", "first desc"));
        input = new StubInput(new String[]{"1", "", "6", "y"});

        new StartUI(input, tracker).init();

        Item item = tracker.findById(id).get();

        output = new String(out.toByteArray());
        expected = " Order's name: " + item.getName()
                + ", id = " + id
                + ", description: " + item.getDescription()
                + ", date of creation: " + new Date(item.getCreated()).toString();

        assertTrue(output.contains(expected));
    }

    /**
     * Test. Searching Item by Id.
     */
    @Test
    public void whenUserSearchItemByIdThenPrintingThatItemInConsole() {

        tracker.add(new Item("Order name", "order description"));
        tracker.add(new Item("other order name", "other desc"));

        String id = tracker.add(new Item("that order", "that description"));
        Item item = tracker.findById(id).get();

        input = new StubInput(new String[]{"4", item.getId(), "", "6", "y"});
        new StartUI(input, tracker).init();

        output = new String(out.toByteArray());
        expected = " Order's name: " + item.getName()
                + ", id = " + id
                + ", description: " + item.getDescription()
                + ", date of creation: " + new Date(item.getCreated()).toString();

        assertTrue(output.contains(expected));
    }

    /**
     * Test. Searching Item by name.
     */
    @Test
    public void whenUserSearchItemByNameThemPrintingAllItemsWithThatNameInConsole() {

        String firstItemId = tracker.add(new Item("Order name", "order description"));
        tracker.add(new Item("other order name", "other desc"));
        String secondItemId = tracker.add(new Item("Order name", "new description"));

        input = new StubInput(new String[]{"5", "Order name", "", "6", "y"});

        new StartUI(input, tracker).init();

        Item firstItem = tracker.findById(firstItemId).get();
        Item secondItem = tracker.findById(secondItemId).get();

        output = new String(out.toByteArray());

                // first order
        expected = new StringJoiner("")
                .add(" Order's name: ")
                .add(firstItem.getName())
                .add(", id = ")
                .add(firstItemId)
                .add(", description: ")
                .add(firstItem.getDescription())
                .add(", date of creation: ")
                .add(new Date(firstItem.getCreated()).toString())
                .add(System.lineSeparator())
                .toString();

                // second order
        String expectedSecond = new StringJoiner("").add(" Order's name: ")
                .add(secondItem.getName())
                .add(", id = ")
                .add(secondItemId)
                .add(", description: ")
                .add(secondItem.getDescription())
                .add(", date of creation: ")
                .add(new Date(secondItem.getCreated()).toString())
                .toString();

        assertTrue(output.contains(expected));
        assertTrue(output.contains(expectedSecond));
    }
}
