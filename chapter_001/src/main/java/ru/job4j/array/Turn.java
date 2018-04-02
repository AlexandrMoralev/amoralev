package ru.job4j.array;

/**
 * Turn
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Turn {

    /**
     * Method back - invert int[] array
     * @param array
     * @return inverted int[] array
     */
    public int[] back(int[] array) {
        int tmp;

        for (int i = 0, j = array.length - 1; i < array.length / 2; i++, j--) {
            tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }

        return array;
    }
}
