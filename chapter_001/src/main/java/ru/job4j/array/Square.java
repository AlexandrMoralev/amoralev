package ru.job4j.array;

/**
 * Square
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Square {

    /**
     * Method calculate
     *
     * @param bound int number - size of output array
     * @return array, filled with the squares of numbers from 1 to bound
     */
    public int[] calculate(int bound) {
        int[] rst = new int[bound];

        for (int index = 1; index < bound + 1; index++) {
            rst [index - 1] = index * index;
        }

        return rst;
    }
}
