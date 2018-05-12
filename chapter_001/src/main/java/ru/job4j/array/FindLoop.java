package ru.job4j.array;

/**
 * FindLoop
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class FindLoop {

    /**
     * Method indexOf
     * @param data - int[] array, where searching the index of element
     * @param el - element of int[] array, which index to find
     * @return int index of element, or -1 if there's no element in array
     */
    public int indexOf(int[] data, int el) {
        int rsl = -1;

        for (int index = 0; index < data.length; index++) {
            if (data[index] == el) {
                rsl = index;
                break;
            }
        }

        return rsl;
    }
}
