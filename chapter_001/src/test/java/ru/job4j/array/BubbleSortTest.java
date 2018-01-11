package ru.job4j.array;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;

/**
 * BubbleSortTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class BubbleSortTest {

    /**
     * Test. Bubble sorting of int[] array
     */
    @Test
    public void whenSortArrayWithTenElementsThenSortedArray() {
        BubbleSort bubbleTest = new BubbleSort();
        int[] result = bubbleTest.sort(new int[] {1, 5, 4, 2, 3, 1, 7, 8, 0, 5});
        assertThat(result, is(new int[] {0, 1, 1, 2, 3, 4, 5, 5, 7, 8}));
    }
}
