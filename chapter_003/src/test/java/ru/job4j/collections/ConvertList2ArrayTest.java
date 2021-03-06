package ru.job4j.collections;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

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

    /**
     * Test. Converting List<int[]> into List<Integer>.
     */
    @Test
    public void whenListOfIntArraysThenListOfIntegers() {
        ConvertList2Array converter = new ConvertList2Array();

        List<int[]> list = new ArrayList<>();
        list.add(new int[]{1, 2});
        list.add(new int[]{});
        list.add(new int[]{3, 4, 5, 6});

        List<Integer> result = converter.convert(list);

        List<Integer> expected = new ArrayList<>();
        for (int i = 1; i < 7; i++) {
            expected.add(i);
        }
        assertThat(result, is(expected));
    }

    /**
     * Test. When input List is empty, returns empty List<Integers>.
     */
    @Test
    public void whenListOfIntArraysIsEmptyThenEmptyListOfIntegers() {
        ConvertList2Array converter = new ConvertList2Array();

        List<int[]> list = new ArrayList<>();
        list.add(new int[]{});

        List<Integer> result = converter.convert(list);

        List<Integer> expected = new ArrayList<>();

        assertThat(result, is(expected));
    }
}
