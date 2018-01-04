package ru.job4j.max;

/**
 * Max
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */

public class Max {

    /**
     * Method max.
     * @param first int number
     * @param second int number
     * @return max of two numbers
     */
    public int max(int first, int second) {
        return (first >= second) ? first : second;
    }

    /**
     * Method max.
     * @param first int number
     * @param second int number
     * @param third int number
     * @return max of three numbers
     */
    public int max(int first, int second, int third) {
        return (max(max(first, second), third));
    }
}
