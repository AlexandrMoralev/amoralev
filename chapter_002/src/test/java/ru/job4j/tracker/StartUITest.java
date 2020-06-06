package ru.job4j.tracker;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    @BeforeEach
    public void setBufferedOutput() {
        System.setOut(new PrintStream(out));
    }

    /**
     * Method trackerInit - creates new Tracker instance
     */
    @BeforeEach
    public void trackerInit() {
        this.tracker = new Tracker();
    }

    /**
     * Method setStdOutputBack - replaces "buffered" out back to std System.out after test
     */
    @AfterEach
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
        Integer id = tracker.add(new Item("test name", "desc"));
        input = new StubInput(new String[]{"2", String.valueOf(id), "new test name", "new desc", "", "6", "y"});
        new StartUI(input, tracker).init();
        assertThat(tracker.findById(id).get().getName(), is("new test name"));
    }

    /**
     * Test. Deleting Item.
     */
    @Test
    public void wheUserDeleteItemThenTrackerHasNoItemWithThatId() {
        Integer id = tracker.add(new Item("test name", "desc"));
        tracker.add(new Item("other name", "other desc"));
        input = new StubInput(new String[]{"3", String.valueOf(id), "", "6", "y"});
        new StartUI(input, tracker).init();
        assertEquals("other name", tracker.findAll().stream().findFirst().get().getName());
    }

    /**
     * Test. Printing all Tracker Items.
     */
    @Test
    public void whenUserChecksAllItemsThenPrintingAllItemsInConsole() {

        Integer id = tracker.add(new Item("first name", "first desc"));
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

        Integer id = tracker.add(new Item("that order", "that description"));
        Item item = tracker.findById(id).get();

        input = new StubInput(new String[]{"4", String.valueOf(item.getId()), "", "6", "y"});
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

        Integer firstItemId = tracker.add(new Item("Order name", "order description"));
        tracker.add(new Item("other order name", "other desc"));
        Integer secondItemId = tracker.add(new Item("Order name", "new description"));

        input = new StubInput(new String[]{"5", "Order name", "", "6", "y"});

        new StartUI(input, tracker).init();

        Item firstItem = tracker.findById(firstItemId).get();
        Item secondItem = tracker.findById(secondItemId).get();

        output = new String(out.toByteArray());

                // first order
        expected = " Order's name: " + firstItem.getName()
                + ", id = " + firstItemId.toString()
                + ", description: " + firstItem.getDescription()
                + ", date of creation: " + new Date(firstItem.getCreated()).toString()
                + System.lineSeparator();

                // second order
        String expectedSecond = " Order's name: " + secondItem.getName()
                + ", id = " + secondItemId.toString()
                + ", description: " + secondItem.getDescription()
                + ", date of creation: " + new Date(secondItem.getCreated()).toString();

        assertTrue(output.contains(expected));
        assertTrue(output.contains(expectedSecond));
    }
}
