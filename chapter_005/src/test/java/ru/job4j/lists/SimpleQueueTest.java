package ru.job4j.lists;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @BeforeEach
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

    @Test
    public void whenPushNullThenThrowsIAException() {
        assertThrows(IllegalArgumentException.class,
                () -> queue.push(null)
        );
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

    @Test
    public void whenPollFromEmptyQueueThenThrowsNSEException() {
        assertThrows(NoSuchElementException.class,
                () -> queue.poll()
        );
    }

    @Test
    public void whenSequentialPushPopElements() {
        queue.push(first);
        assertThat(queue.poll().equals(first), is(true));
        queue.push(third);
        assertThat(queue.poll().equals(third), is(true));
    }
}
