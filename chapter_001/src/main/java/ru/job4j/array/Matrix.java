package ru.job4j.array;

/**
 * Matrix
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Matrix {

    /**
     * Method multiple
     * @param size - int dimension of a two-dimensional array
     * @return multiplication table of a given size
     */
    public int[][] multiple(int size) {
        int[][] result = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i][j] = (i + 1) * (j + 1);
            }
        }

        return result;
    }
}
