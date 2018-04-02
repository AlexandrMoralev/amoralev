package ru.job4j.loop;

import java.util.function.BiPredicate;

/**
 * Paint
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Paint {

    private String loopBy(int height, int width, BiPredicate<Integer, Integer> predict) {
        StringBuilder screen = new StringBuilder();

        for (int row = 0; row != height; row++) {
            for (int column = 0; column != width; column++) {
                if (predict.test(row, column)) {
                    screen.append("^");
                } else {
                    screen.append(" ");
                }
            }
            screen.append(System.lineSeparator());
        }

        return screen.toString();
    }

    /**
     * Method rightTrl - makes right triangle of the pyramid
     *
     * @param height int dimension of triangle
     * @return String
     */
    public String rightTrl(int height) {
        return this.loopBy(
                height,
                height,
                (row, column) -> row >= column
        );
    }

    /**
     * Method leftTrl - makes left triangle of the pyramid
     *
     * @param height int dimension if triangle
     * @return String
     */
    public String leftTrl(int height) {
        return this.loopBy(
                height,
                height,
                (row, column) -> row >= height - column - 1
        );
    }

    /**
     * Method pyramid - makes pyramid from [^] symbols
     *
     * @param height - int dimension of the pyramid
     * @return String
     */
    public String pyramid(int height) {
        return this.loopBy(
                height,
                2 * height - 1,
                (row, column) -> row >= height - column - 1 && column <= height + row - 1
        );
    }
}
