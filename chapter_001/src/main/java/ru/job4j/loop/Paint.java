package ru.job4j.loop;

/**
 * Paint
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Paint {

    /**
     * Method rightTrl - makes right triangle of the pyramid
     *
     * @param height int dimension of triangle
     * @return String
     */
    public String rightTrl(int height) {
        StringBuilder screen = new StringBuilder();
        int width = height;

        for (int row = 0; row != height; row++) {
            for (int column = 0; column < width; column++) {
                if (row >= column) {
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
     * Method leftTrl - makes left triangle of the pyramid
     *
     * @param height int dimension if triangle
     * @return String
     */
    public String leftTrl(int height) {
        StringBuilder screen = new StringBuilder();
        int width = height;

        for (int row = 0; row != width; row++) {
            for (int column = 0; column != width; column++) {
                if (row >= width - column -1) {
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
     * Method pyramid - makes pyramid from [^] symbols
     *
     * @param height - int dimension of the pyramid
     * @return String
     */
    public String pyramid(int height) {
        StringBuilder screen = new StringBuilder();
        int width = 2 * height - 1;

        for (int row = 0; row != height; row++) {
            for (int column = 0; column != width ; column++) {
                if (row >= height - column - 1 && column <= row + height - 1) {
                    screen.append("^");
                } else {
                    screen.append(" ");
                }
            }
            screen.append(System.lineSeparator());
        }
        return screen.toString();
    }
}
