package ru.job4j.lists;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * SimpleStackTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class SimpleStackTest {

    private SimpleStack<String> stack;

    @BeforeEach
    public void init() {
        stack = new SimpleStack<>();
    }

    @Test
    public void whenPushTwoElementsThenStackSizeIsTwo() {
        stack.push("first");
        stack.push("second");
        assertThat(stack.size(), is(2));
    }

    @Test
    public void whenPollElementShouldReturnElement() {
        stack.push("first");
        stack.push("second");
        assertThat(stack.poll(), is("second"));
        assertThat(stack.poll(), is("first"));
    }

    @Test
    public void whenPollElementFromEmptyStackThenThrowNSEException() {
        assertThrows(NoSuchElementException.class,
                () -> stack.poll()
        );
    }
}
