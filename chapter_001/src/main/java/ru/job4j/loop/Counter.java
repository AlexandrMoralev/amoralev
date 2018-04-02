package ru.job4j.loop;

/**
 * Counter
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Counter {

    /**
     * Method add.
     * @param start int number
     * @param finish int number
     * @return sum of even numbers inside the start - finish range
     */
    public int add(int start, int finish) {
        int evenSum = 0;

        for (int i = start; i <= finish; i++) {
            if (i % 2 == 0) {
                evenSum += i;
            }
        }

        return evenSum;
    }
}
