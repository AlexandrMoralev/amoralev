package ru.job4j.collections;

import java.util.ArrayList;
import java.util.List;

/**
 * ConvertList2Array
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class ConvertList2Array {
    /**
     * Method toArray - converts List of Integers to int[][] array
     * method evenly divides the List by the number of rows in a two-dimensional array
     * if size of the result array is larger than size of the List, the empty elements of the array are filled with zeros
     * @param list List of Integers to convert
     * @param rows rows at result array
     * @return int[][] converted from source List
     */
    public int[][] toArray(List<Integer> list, int rows) {

        if (list.isEmpty()) {
            throw new IllegalArgumentException("List is empty.");
        }
        if (rows <= 0) {
            throw new IllegalArgumentException("Rows number must be greater than 0");
        }

        int cells = (list.size() % rows == 0)
                ? (list.size() / rows)
                : (list.size() / rows + 1);

        int[][] array = new int[rows][cells];

        int rowPosition = 0;
        int columnPosition = 0;

        for (Integer num : list) {
            if (columnPosition == cells) {
                columnPosition = 0;
                rowPosition++;
            }
            array[rowPosition][columnPosition++] = num;
        }
        return array;
    }

    /**
     * Method convert - converts List<int[]> into List<Integer>
     * @param list List<int[]> to convert
     * @return List<Integer> converted from input List<int[]>,
     * returns empty List<Integer>, when input List is empty
     */
    public List<Integer> convert(List<int[]> list) {

        List<Integer> result = new ArrayList<>();

        for (int[] array : list) {
            for (int element : array) {
                result.add(element);
            }
        }
        return result;
    }
}
