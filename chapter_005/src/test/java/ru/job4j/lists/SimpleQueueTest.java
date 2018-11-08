package ru.job4j.lists;

import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * SimpleQueueTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class SimpleQueueTest {

    private SimpleQueue<String> queue;
    private String first = "first";
    private String second = "second";
    private String third = "third";

    @Before
    public void init() {
        queue = new SimpleQueue<>();
    }

    @Test
    public void whenPushThreeItemsThenSizeIsThree() {
        queue.push(first);
        queue.push(second);
        queue.push(third);
        assertThat(queue.size(), is(3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenPushNullThenThrowsIAException() {
        queue.push(null);
    }

    @Test
    public void whenPollThreeItemsShouldReturnThreeItems() {
        queue.push(first);
        queue.push(second);
        queue.push(third);
        assertThat(queue.poll().equals(first), is(true));
        assertThat(queue.poll().equals(second), is(true));
        assertThat(queue.poll().equals(third), is(true));
    }

    @Test(expected = NoSuchElementException.class)
    public void whenPollFromEmptyQueueThenThrowsNSEException() {
        queue.poll();
    }

    @Test
    public void whenSequentialPushPopElements() {
        queue.push(first);
        assertThat(queue.poll().equals(first), is(true));
        queue.push(third);
        assertThat(queue.poll().equals(third), is(true));
    }
}
