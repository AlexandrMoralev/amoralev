package ru.job4j.max;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

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
        assertThat(result, is(2));
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

    /**
     * Test First is max
     */
    @Test
    public void whenFirstIsMax() {
        Max max = new Max();
        int rsl = max.max(3, 2, 1);
        assertThat(rsl, is(3));
    }

    /**
     * Test Second is max
     */
    @Test
    public void whenSecondIsMax() {
        Max max = new Max();
        int rsl = max.max(0, 3, -1);
        assertThat(rsl, is(3));
    }

    /**
     * Test Second is max
     */
    @Test
    public void whenThirdIsMax() {
        Max max = new Max();
        int rsl = max.max(0, 1, 2);
        assertThat(rsl, is(2));
    }

    /**
     * Test TwoFromThreeIsEqual
     */
    @Test
    public void whenTwoFromThreeIsEqual() {
        Max max = new Max();
        int rsl = max.max(1, 0, 1);
        assertThat(rsl, is(1));
    }
}
