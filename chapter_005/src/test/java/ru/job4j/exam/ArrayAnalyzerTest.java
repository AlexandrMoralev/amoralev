package ru.job4j.exam;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * ArrayAnalyzer
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class ArrayAnalyzerTest {

    private ArrayAnalyzer<String> strAnalyzer;
    private ArrayAnalyzer<Integer> intAnalyzer;
    private final String[] sourceStrArr = {"one", "two", "four", "seven", " "};
    private final String[] editedStrArr = {"one", "four"};
    private final String[] anotherEditedStrArr = {"one", "two", "four", "seven", " "};
    private final String[] zeroStrArr = new String[0];

    private final Integer[] src = new Integer[100000];
    private final Integer[] edited = new Integer[100000];

    private final String[] srcStr = fillSrcArray(new String[100000]);
    private final String[] editedStr = fillEditedArray(new String[100000]);

    @BeforeEach
    public void init() {
        strAnalyzer = new ArrayAnalyzer<>();
        intAnalyzer = new ArrayAnalyzer<>();
        Arrays.fill(src, 130);
        Arrays.fill(edited, 1000);
    }

    private String[] fillSrcArray(String[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            arr[i] = "" + i;
        }
        return arr;
    }

    private String[] fillEditedArray(String[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            arr[i] = "" + 2 * i;
        }
        return arr;
    }

    // findDeleted() tests
    @Test
    public void whenThreeElementsDeletedFromArrayShouldReturnListWithDeleted() {
        assertThat(strAnalyzer.findDeleted(sourceStrArr, editedStrArr), is(new ArrayList<>(Arrays.asList("two", "seven", " "))));
    }

    @Test
    public void whenNoneElementsDeletedFromArrayShouldReturnEmptyList() {
        assertThat(strAnalyzer.findDeleted(sourceStrArr, anotherEditedStrArr), is(Collections.EMPTY_LIST));
    }

    @Test
    public void whenFirstArrayIsEmptyShouldReturnEmptyList() {
        assertThat(strAnalyzer.findDeleted(zeroStrArr, sourceStrArr), is(Collections.EMPTY_LIST));
    }

    @Test
    public void whenSecondArrayIsEmptyShouldReturnEmptyList() {
        assertThat(strAnalyzer.findDeleted(anotherEditedStrArr, zeroStrArr), is(Collections.EMPTY_LIST));
    }

    @Test
    public void whenFirstArrIsNullShouldThrowIAException() {
        assertThrows(IllegalArgumentException.class,
                () -> strAnalyzer.findDeleted(null, sourceStrArr)
        );
    }

    @Test
    public void whenSecondArrIsNullShouldThrowIAException() {
        assertThrows(IllegalArgumentException.class,
                () -> strAnalyzer.findDeleted(sourceStrArr, null)
        );
    }


    // findDeletedSlowly() tests
    @Disabled // run manually
    @Test
    public void whenThreeElementsDeletedFromArrayThenReturnListWithDeleted() {
        assertThat(strAnalyzer.findDeletedSlowly(sourceStrArr, editedStrArr), is(new ArrayList<>(Arrays.asList("two", "seven", " "))));
    }

    @Disabled // run manually
    @Test
    public void whenNoneElementsDeletedFromArrayThenReturnEmptyList() {
        assertThat(strAnalyzer.findDeletedSlowly(sourceStrArr, anotherEditedStrArr), is(Collections.EMPTY_LIST));
    }

    @Disabled // run manually
    @Test
    public void whenFirstArrayIsEmptyThenReturnEmptyList() {
        assertThat(strAnalyzer.findDeletedSlowly(zeroStrArr, sourceStrArr), is(Collections.EMPTY_LIST));
    }

    @Disabled // run manually
    @Test
    public void whenSecondArrayIsEmptyThenReturnEmptyList() {
        assertThat(strAnalyzer.findDeletedSlowly(anotherEditedStrArr, zeroStrArr), is(Collections.EMPTY_LIST));
    }

    @Disabled // run manually
    @Test
    public void whenFirstArrIsNullThenThrowIAException() {
        assertThrows(IllegalArgumentException.class,
                () -> strAnalyzer.findDeletedSlowly(null, sourceStrArr)
        );
    }

    @Disabled // run manually
    @Test
    public void whenSecondArrIsNullThenThrowIAException() {
        assertThrows(IllegalArgumentException.class,
                () -> strAnalyzer.findDeletedSlowly(sourceStrArr, null)
        );
    }


    // Integer Tests
    @Test
    public void whenFindDeletedFrom10KArrayShouldReturnListWithDeleted() {
        Long start = System.nanoTime();
        intAnalyzer.findDeleted(src, edited);
        Long finish = System.nanoTime();
        System.out.println("@IntegerTests findDeleted: " + (finish - start) / 1000000 + " ms");
    }

    @Disabled // run manually
    @Test
    public void whenFindDeletedSlowFrom10KArrayShouldReturnListWithDeleted() {
        Long start = System.nanoTime();
        intAnalyzer.findDeletedSlowly(src, edited);
        Long finish = System.nanoTime();
        System.out.println("@IntegerTests findDeletedSlowly: " + (finish - start) / 1000000 + " ms");
    }

    // String Tests
    @Test
    public void whenFindFrom10KShouldReturnListWDeleted() {
        Long start = System.nanoTime();
        strAnalyzer.findDeleted(srcStr, editedStr);
        Long finish = System.nanoTime();
        System.out.println("@StringTests findDeleted: " + (finish - start) / 1000000 + " ms");
    }

    @Disabled // run manually
    @Test
    public void whenFindSlowFrom10KArrayShouldReturnListWDeleted() {
        Long start = System.nanoTime();
        strAnalyzer.findDeletedSlowly(srcStr, editedStr);
        Long finish = System.nanoTime();
        System.out.println("@StringTests findDeletedSlowly: " + (finish - start) / 1000000 + " ms");
    }
}
