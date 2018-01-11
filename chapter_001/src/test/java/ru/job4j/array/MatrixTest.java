package ru.job4j.array;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;

/**
 * MatrixTest
 *
 * @author
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
                {0, 0, 0, 0, 0},
                {0, 1, 2, 3, 4},
                {0, 2, 4, 6, 8},
                {0, 3, 6, 9, 12},
                {0, 4, 8, 12, 16}
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
