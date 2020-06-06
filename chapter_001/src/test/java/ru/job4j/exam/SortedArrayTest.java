package ru.job4j.exam;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * SortedArrayTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class SortedArrayTest {

    /**
     * Test. When adding the first array first.
     */
    @Test
    public void whenFirstArrayShorterThanSecondThenOneMergedArray() {
        SortedArray sortedArray = new SortedArray();
        int[] result = sortedArray.mergeTwoSortedArrays(
                new int[]{0, 1, 3, 5},
                new int[]{2, 4, 5, 6, 7}
        );
        int[] expected = {0, 1, 2, 3, 4, 5, 5, 6, 7};
        assertThat(result, is(expected));
    }

    /**
     * Test. When adding the second array first.
     */
    @Test
    public void whenSecondArrayShorterThanFirstThenOneMergedArray() {
        SortedArray sortedArray = new SortedArray();
        int[] result = sortedArray.mergeTwoSortedArrays(
                new int[] {-1, 0, 0, 2},
                new int[] {0, 1, 3}
        );
        int[] expected = {-1, 0, 0, 0, 1, 2, 3};
        assertThat(result, is(expected));
    }

    /**
     * Test. When adding overlapping elements of equal length arrays
     */
    @Test
    public void whenTwoArraysEqualLengthThenOneMergedArray() {
        SortedArray sortedArray = new SortedArray();
        int[] result = sortedArray.mergeTwoSortedArrays(
                new int[] {1, 3, 5, 8},
                new int[] {1, 2, 4, 8}
        );
        int[] expected = {1, 1, 2, 3, 4, 5, 8, 8};
        assertThat(result, is(expected));
    }

    /**
     * Test. Arrays with non-overlapping elements.
     */
    @Test
    public void whenTwoArraysWithNonOverlappingValueRangesThenOneMergedArray() {
        SortedArray sortedArray = new SortedArray();
        int[] result = sortedArray.mergeTwoSortedArrays(
                new int[] {0, 1, 2, 3},
                new int[] {5, 6, 7}
        );
        int[] expected = {0, 1, 2, 3, 5, 6, 7};
        assertThat(result, is(expected));
    }
}
