package ru.job4j.array;

/**
 * Matrix
 *
 * @author
 * @version $Id$
 * @since 0.1
 */
public class Matrix {

    /**
     * Method multiple
     * @param size - int dimension of a two-dimensional array
     * @return multiplication table of a given size
     */
    int[][] multiple(int size) {
        int[][] result = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i][j] = i * j;
            }
        }

        return result;
    }
}
