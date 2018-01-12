package ru.job4j.array;

import java.util.Arrays;

/**
 * ArrayDuplicates
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class ArrayDuplicate {

    /**
     * Method remove - removes duplicates from String[] array
     * @param array - input String[] array
     * @return String[] array without duplicates
     */
    public String[] remove(String[] array) {
        int uniqueElements = array.length;

        for (int out = 0; out < uniqueElements; out++) {
            for (int inner = out + 1; inner < uniqueElements; inner++) {
                if (array[out].equals(array[inner])) {
                    array[inner] = array[uniqueElements - 1];
                    uniqueElements--;
                }
            }
        }
        return Arrays.copyOf(array, uniqueElements);
    }
}
