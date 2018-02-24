package ru.job4j.tracker;

import org.junit.Test;
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

    @Test
    public void whenUserAddItemThenTrackerHasNewItemWithSameName() {
        Tracker tracker = new Tracker();
        Input input = new StubInput(new String[]{"0", "test name", "desc", "6"});
        new StartUI(input, tracker).init();
        assertThat(tracker.findAll()[0].getName(), is("test name"));
    }

    @Test
    public void whenUserEditItemThenTrackerHasUpdatedValue() {
        Tracker tracker = new Tracker();
        Item item = tracker.add(new Item("test name", "desc"));
        Input input = new StubInput(new String[]{"2", item.getId(), "new test name", "new desc", "6"});
        new StartUI(input, tracker).init();
        assertThat(tracker.findById(item.getId()).getName(), is("new test name"));
    }

    @Test
    public void wheUserDeleteItemThenTrackerHasNoItemWithThatId() {
        Tracker tracker = new Tracker();
        Item item = tracker.add(new Item("test name", "desc"));
        tracker.add(new Item("other name", "other desc"));
        Input input = new StubInput(new String[]{"3", item.getId(), "6"});
        new StartUI(input, tracker).init();
        assertThat(tracker.findAll()[0].getName(), is("other name"));
    }
}
