package ru.job4j.array;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;

/**
 * MatrixTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class MatrixTest {

    /**
     * Test. Matrix 5x5.
     */
    @Test
    public void whenSizeFiveThenMatrixFiveByFive() {
        Matrix matrix = new Matrix();
        int[][] result = matrix.multiple(5);
        int[][] expected = {
                {1, 2, 3, 4, 5},
                {2, 2 * 2, 2 * 3, 2 * 4, 2 * 5},
                {3, 3 * 2, 3 * 3, 3 * 4, 3 * 5},
                {4, 4 * 2, 4 * 3, 4 * 4, 4 * 5},
                {5, 5 * 2, 5 * 3, 5 * 4, 5 * 5}
        };
        assertThat(result, is(expected));
    }

    /**
     * Test. Matrix size = 0.
     */
    @Test
    public void whenSizeIsZeroThenZeroMatrix() {
        Matrix matrix = new Matrix();
        int[][] result = matrix.multiple(0);
        assertThat(result, is(new int[0][0]));
    }
}
