package ru.job4j.collections;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * ConvertList2ArrayTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class ConvertList2ArrayTest {

    /**
     * Test. When List.size() == 7, then output is int[3][3].
     */
    @Test
    public void when7ElementsThen9() {
        ConvertList2Array list = new ConvertList2Array();
        int[][] result = list.toArray(
                Arrays.asList(1, 2, 3, 4, 5, 6, 7),
                3
        );
        int[][] expect = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 0, 0}
        };
        assertThat(result, is(expect));
    }

    /**
     * Test. When List.size() == 9, then output is int[3][3].
     */
    @Test
    public void when9ElementsThen9() {
        ConvertList2Array list = new ConvertList2Array();
        int[][] result = list.toArray(
                Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9),
                3
        );
        int[][] expect = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        assertThat(result, is(expect));
    }

    /**
     * Test. When List.size() == 8, then output is int[2][4].
     */
    @Test
    public void when8ElementsThen2() {
        ConvertList2Array list = new ConvertList2Array();
        int[][] result = list.toArray(
                Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8),
                2
        );
        int[][] expected = {
                {1, 2, 3, 4},
                {5, 6, 7, 8}
        };
        assertThat(result, is(expected));
    }

    /**
     * Test. When List.size() == 8, then output is int[1][8].
     */
    @Test
    public void when1ElementsThen1() {
        ConvertList2Array list = new ConvertList2Array();
        int[][] result = list.toArray(
                Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8),
                1
        );
        int[][] expected = {{1, 2, 3, 4, 5, 6, 7, 8}};
        assertThat(result, is(expected));
    }

    /**
     * Test. When rows == 0, then throws IllegalArgumentException().
     */
    @Test
    public void whenRowIs0ThenIAException() {
        ConvertList2Array list = new ConvertList2Array();

        String expected = "Rows number must be greater than 0";
        String result = "";

        try {
            list.toArray(
                    Arrays.asList(1, 2, 3, 4),
                    0
            );
        } catch (IllegalArgumentException e) {
            result = e.getMessage();
        } finally {
            assertThat(result, is(expected));
        }
    }

    /**
     * Test. When List is empty, then throws IllegalArgumentException().
     */
    @Test
    public void whenListIsEmptyThenIAException() {
        ConvertList2Array list = new ConvertList2Array();

        String expected = "List is empty.";
        String result = "";

        try {
            list.toArray(Collections.EMPTY_LIST, 2);
        } catch (IllegalArgumentException e) {
            result = e.getMessage();
        } finally {
            assertThat(result, is(expected));
        }
    }

}
