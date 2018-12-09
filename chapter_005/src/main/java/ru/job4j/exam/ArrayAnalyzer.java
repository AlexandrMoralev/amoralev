package ru.job4j.exam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * ArrayAnalyzer
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class ArrayAnalyzer<T> {

    // Задание 3: Даны 2 массива - 1й исходный, 2ой это первый, из которого удалены элементы.
    // Определить, какие элементы были удалены.

    /**
     * Method findDeleted - checks for differences between two arrays and finds all removed elements
     *
     * @param source T array to compare with
     * @param edited T array to be checked
     * @return List of elements removed from the source array, or an EmptyList
     */
    public List<T> findDeleted(final T[] source, final T[] edited) {
        boolean validated = isArgValid(source) && isArgValid(edited);
        List<T> src = Collections.EMPTY_LIST;
        List<T> edit;
        if (validated) {
            src = new ArrayList<>(Arrays.asList(source));
            edit = new ArrayList<>(Arrays.asList(edited));
            validated = src.removeAll(edit);
        }
        return validated ? src : Collections.EMPTY_LIST;
    }

    private boolean isArgValid(T[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException();
        }
        return arr.length != 0;
    }
}
