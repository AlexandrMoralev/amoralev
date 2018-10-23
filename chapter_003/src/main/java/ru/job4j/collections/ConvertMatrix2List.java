package ru.job4j.collections;

import java.util.ArrayList;
import java.util.List;

/**
 * ConvertMatrix2List
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class ConvertMatrix2List {

    /**
     * Method toList - converts two-dimensional int[][] array into ArrayList<Integer>
     *
     * @param array int[][] array to convert
     * @return ArrayList<Integer> converted from the source array
     */
    public List<Integer> toList(int[][] array) {

        if (array == null || array.length == 0 || array[0].length == 0) {
            throw new IllegalArgumentException("the source array is empty or null");
        }

        List<Integer> list = new ArrayList<>();

        for (int[] rows : array) {
            for (int num : rows) {
                list.add(num);
            }
        }
        return list;
    }
}
