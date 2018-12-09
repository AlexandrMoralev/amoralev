package ru.job4j.exam;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * ArrayAnalyzer
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class ArrayAnalyzerTest {

    private ArrayAnalyzer<String> analyzer;
    private final String[] sourceStrArr = {"one", "two", "four", "seven", " "};
    private final String[] editedStrArr = {"one", "four"};
    private final String[] anotherEditedStrArr = {"one", "two", "four", "seven", " "};
    private final String[] zeroStrArr = new String[0];

    @Before
    public void init() {
        analyzer = new ArrayAnalyzer<>();
    }

    @Test
    public void whenThreeElementsDeletedFromArrayShouldReturnListWithDeleted() {
        assertThat(analyzer.findDeleted(sourceStrArr, editedStrArr), is(new ArrayList<>(Arrays.asList("two", "seven", " "))));
    }

    @Test
    public void whenNoneElementsDeletedFromArrayShouldReturnEmptyList() {
        assertThat(analyzer.findDeleted(sourceStrArr, anotherEditedStrArr), is(Collections.EMPTY_LIST));
    }

    @Test
    public void whenFirstArrayIsEmptyShouldReturnEmptyList() {
        assertThat(analyzer.findDeleted(zeroStrArr, sourceStrArr), is(Collections.EMPTY_LIST));
    }

    @Test
    public void whenSecondArrayIsEmptyShouldReturnEmptyList() {
        assertThat(analyzer.findDeleted(anotherEditedStrArr, zeroStrArr), is(Collections.EMPTY_LIST));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenFirstArrIsNullShouldThrowIAException() {
        analyzer.findDeleted(null, sourceStrArr);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenSecondArrIsNullShouldThrowIAException() {
        analyzer.findDeleted(sourceStrArr, null);
    }
}
