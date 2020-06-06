package ru.job4j.array;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * SquareTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class SquareTest {

    /**
     * Test. When bound = 10.
     */
    @Test
    public void whenBoundTenThenOneToOneHundred() {
        Square square = new Square();
        int[] result = square.calculate(10);
        int[] expected = new int[] {1, 4, 9, 16, 25, 36, 49, 64, 81, 100};
        assertThat(result, is(expected));
    }

    /**
     * Test. When bound = 0.
     */
    @Test
    public void whenBoundZeroThenZero() {
        Square square = new Square();
        int[] result = square.calculate(0);
        int[] expected = new int[0];
        assertThat(result, is(expected));
    }
}
