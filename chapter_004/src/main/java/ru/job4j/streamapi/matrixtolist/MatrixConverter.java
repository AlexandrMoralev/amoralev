package ru.job4j.streamapi.matrixtolist;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MatrixConverter
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class MatrixConverter {

    public static List<Integer> convertToList(Integer[][] matrix) {
        return Arrays.stream(matrix).flatMap(Arrays::stream).collect(Collectors.toList());
    }
}
