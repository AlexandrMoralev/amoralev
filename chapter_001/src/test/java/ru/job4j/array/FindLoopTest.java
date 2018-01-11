package ru.job4j.array;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.is;

/**
 * FindLoopTest
 *
 * @author
 * @version $Id$
 * @since 0.1
 */
public class FindLoopTest {

    /**
     * Test. When element is in array
     */
    @Test
    public void whenElementIndexIsFiveThenIndexIsFive() {
        FindLoop fl = new FindLoop();
        int[] array = new int[] {0, 1, 2, 3, 4, 5, 6};
        int result = fl.indexOf(array, 5);
        assertThat(result, is(5));
    }

    /**
     * Test. There is no such element in array
     */
    @Test
    public void whenElementIsAbsentThenMinusOne() {
        FindLoop fl = new FindLoop();
        int[] array = new int[] {0, 1, 2, 3, 4, 5, 6};
        int result = fl.indexOf(array,10);
        assertThat(result, is(-1));
    }
}
