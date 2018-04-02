package ru.job4j.loop;

/**
 * Factorial
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Factorial {

    /**
     * Method calc
     *
     * @param n - int number, n > 0
     * @return the factorial of a number n
     */
    public int calc(int n) {
        int result = 1;

        for (int i = 1; i <= n; i++) {
            result *= i;
        }

        return result;
    }
}
