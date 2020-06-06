package ru.job4j.streamapi.matrixtolist;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * MatrixConverterTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class MatrixConverterTest {

    @Test
    public void convertMatrixToListTest() {
        Integer[][] matrix = new Integer[][]{
                new Integer[]{1, 2},
                new Integer[]{3, 4}
        };
        assertThat(MatrixConverter.convertToList(matrix), is(List.of(1, 2, 3, 4)));
    }

    @Test
    public void convertEmptyMatrixToListTest() {
        Integer[][] matrix = new Integer[][]{
                new Integer[]{},
                new Integer[]{}
        };
        assertThat(MatrixConverter.convertToList(matrix), is(Collections.emptyList()));
    }
}
