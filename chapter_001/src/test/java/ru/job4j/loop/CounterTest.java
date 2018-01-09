package ru.job4j.loop;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;

/**
 * CounterTest.
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class CounterTest {

    /**
     * Test. Sum of even numbers from 1 to 10.
     */
    @Test
    public void whenSumEvenNumbersFromOneToTenThenThirty () {
        Counter counter = new Counter();
        int result = counter.add(1, 10);
        assertThat (result, is(30));
    }

    /**
     * Test. Sum = 0, when both of numbers are equal.
     */
    @Test
    public void whenStartEqualFinishThenZero () {
        Counter counter = new Counter();
        int result = counter.add(1,1);
        assertThat(result, is(0));
    }

    /**
     * Test. Sum = 0, when start number is greater than finish.
     */
    @Test
    public void whenStartGreaterFinishThanZero () {
        Counter counter = new Counter();
        int result = counter.add(10, -2);
        assertThat(result, is(0));
    }
}
