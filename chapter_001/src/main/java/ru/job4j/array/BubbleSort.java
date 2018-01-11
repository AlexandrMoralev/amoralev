package ru.job4j.array;

/**
 * BubbleSort
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class BubbleSort {

    /**
     * Method sort - sorting array by the BubbleSort algorythm
     * @param array int[]
     * @return sorted array
     */
    public int[] sort(int[] array) {
        int tmp;

        for (int j = array.length - 1; j > 0 ; j--) {
            for (int index = 1; index < array.length ; index++) {
                if (array[index - 1] > array[index]) {
                    tmp = array[index];
                    array[index] = array[index - 1];
                    array[index - 1] = tmp;
                }
            }
        }

        return array;
    }
}
