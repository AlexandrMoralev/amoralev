package ru.job4j.max;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * MaxTest.
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */

public class MaxTest {

    /**
     * Test First > Second
     */
    @Test
    public void whenFirstGreaterThanSecond() {
        Max maximum = new Max();
        int result = maximum.max(2, 1);
        assertThat (result, is(2));
    }

    /**
     * Test First < Second
     */
    @Test
    public void whenFirstLessThanSecond() {
        Max maximum = new Max();
        int result = maximum.max(1, 2);
        assertThat(result, is(2));
    }

    /**
     * Test First == Second
     */
    @Test
    public void whenFirstEqualsSecond() {
        Max maximum = new Max();
        int result = maximum.max(2, 2);
        assertThat(result, is(2));
    }
}
