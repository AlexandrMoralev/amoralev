package ru.job4j.collections;

import org.junit.Test;
import java.util.List;
import java.util.Arrays;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * ConvertMatrix2ListTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class ConvertMatrix2ListTest {

    /**
     * Test. When array int[2][2], then output is List.size() == 4.
     */
    @Test
    public void when2on2ArrayThenList4() {
        ConvertMatrix2List list = new ConvertMatrix2List();
        int[][] input = {
                {1, 2},
                {3, 4}
        };
        List<Integer> expected = Arrays.asList(
                1, 2, 3, 4
        );
        List<Integer> result = list.toList(input);
        assertThat(result, is(expected));
    }

    /**
     * Test. When array int[3][3], then output is List.size() == 9.
     */
    @Test
    public void when3on3ArrayThenList4() {
        ConvertMatrix2List list = new ConvertMatrix2List();
        int[][] input = {
                {1, 2, 3},
                {0, 0, 0},
                {7, 8, 9}
        };
        List<Integer> expected = Arrays.asList(
                1, 2, 3, 0, 0, 0, 7, 8, 9
        );
        List<Integer> result = list.toList(input);
        assertThat(result, is(expected));
    }

    /**
     * Test. When int[][] array is multidimensional, then output List is filled correctly.
     */
    @Test
    public void when2onVariousLengthArrayThenList3() {
        ConvertMatrix2List list = new ConvertMatrix2List();
        int[][] input = {{0}, {1, 2, 3}};
        List<Integer> expected = Arrays.asList(0, 1, 2, 3);
        List<Integer> result = list.toList(input);
        assertThat(result, is(expected));
    }

    /**
     * Test. When int[][] array is empty = {} or {{}}, then throws IllegalArgumentException.
     */
    @Test
    public void whenEmptyArrayThenIAException() {
        ConvertMatrix2List list = new ConvertMatrix2List();
        int[][] input = {{}};

        String expected = "the source array is empty or null";
        String result = "";

        try {
            List<Integer> example = list.toList(input);
        } catch (IllegalArgumentException e) {
            result = e.getMessage();
        } finally {
            assertThat(result, is(expected));
        }
    }

    /**
     * Test. When int[][] array = null, then throws IllegalArgumentException.
     */
    @Test
    public void whenNullArrayReferenceThenIAException() {
        ConvertMatrix2List list = new ConvertMatrix2List();
        int[][] input = null;

        String expected = "the source array is empty or null";
        String result = "";

        try {
            List<Integer> example = list.toList(input);
        } catch (IllegalArgumentException e) {
            result = e.getMessage();
        } finally {
            assertThat(result, is(expected));
        }
    }
}
