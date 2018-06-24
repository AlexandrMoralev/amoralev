package ru.job4j.tracker;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.StringJoiner;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;

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
    private Item item;

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
        assertThat(tracker.findAll().get(0).getName(), is("test name"));
    }

    /**
     * Test. Editing Item.
     */
    @Test
    public void whenUserEditItemThenTrackerHasUpdatedValue() {
        item = tracker.add(new Item("test name", "desc"));
        input = new StubInput(new String[]{"2", item.getId(), "new test name", "new desc", "", "6", "y"});
        new StartUI(input, tracker).init();
        assertThat(tracker.findById(item.getId()).getName(), is("new test name"));
    }

    /**
     * Test. Deleting Item.
     */
    @Test
    public void wheUserDeleteItemThenTrackerHasNoItemWithThatId() {
        item = tracker.add(new Item("test name", "desc"));
        tracker.add(new Item("other name", "other desc"));
        input = new StubInput(new String[]{"3", item.getId(), "", "6", "y"});
        new StartUI(input, tracker).init();
        assertThat(tracker.findAll().get(0).getName(), is("other name"));
    }

    /**
     * Test. Printing all Tracker Items.
     */
    @Test
    public void whenUserChecksAllItemsThenPrintingAllItemsInConsole() {

        item = tracker.add(new Item("first name", "first desc"));
        input = new StubInput(new String[] {"1", "", "6", "y"});

        new StartUI(input, tracker).init();

        output = new String(out.toByteArray());
        expected = new StringJoiner("")
                .add(" Order's name: ")
                .add(item.getName())
                .add(", id = ")
                .add(item.getId())
                .add(", description: ")
                .add(item.getDescription())
                .add(", date of creation: ")
                .add(new Date(item.getCreated()).toString())
                .toString();

        assertThat(output.contains(expected), is(true));
    }

    /**
     * Test. Searching Item by Id.
     */
    @Test
    public void whenUserSearchItemByIdThenPrintingThatItemInConsole() {

        tracker.add(new Item("Order name", "order description"));
        tracker.add(new Item("other order name", "other desc"));
        item = tracker.add(new Item("that order", "that description"));

        input = new StubInput(new String[] {"4", item.getId(), "", "6", "y"});
        new StartUI(input, tracker).init();

        output = new String(out.toByteArray());
        expected = new StringJoiner("")
                .add(" Order's name: ")
                .add(item.getName())
                .add(", id = ")
                .add(item.getId())
                .add(", description: ")
                .add(item.getDescription())
                .add(", date of creation: ")
                .add(new Date(item.getCreated()).toString())
                .toString();

        assertThat(output.contains(expected), is(true));
    }

    /**
     * Test. Searching Item by name.
     */
    @Test
    public void whenUserSearchItemByNameThemPrintingAllItemsWithThatNameInConsole() {

        Item firstItem = tracker.add(new Item("Order name", "order description"));
        tracker.add(new Item("other order name", "other desc"));
        Item secondItem = tracker.add(new Item("Order name", "new description"));

        input = new StubInput(new String[] {"5", "Order name", "", "6", "y"});

        new StartUI(input, tracker).init();

        output = new String(out.toByteArray());
        expected = new StringJoiner("")
                // first order
                .add(" Order's name: ")
                .add(firstItem.getName())
                .add(", id = ")
                .add(firstItem.getId())
                .add(", description: ")
                .add(firstItem.getDescription())
                .add(", date of creation: ")
                .add(new Date(firstItem.getCreated()).toString())
                .add(System.lineSeparator())
                // second order
                .add(" Order's name: ")
                .add(secondItem.getName())
                .add(", id = ")
                .add(secondItem.getId())
                .add(", description: ")
                .add(secondItem.getDescription())
                .add(", date of creation: ")
                .add(new Date(secondItem.getCreated()).toString())
                .toString();

        assertThat(output.contains(expected), is(true));
    }
}
