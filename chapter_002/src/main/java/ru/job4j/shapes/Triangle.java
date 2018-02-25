package ru.job4j.shapes;

/**
 * Triangle
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Triangle implements Shape {
    /**
     * Method draw - concat strings for drawing Triangle
     * @return String to draw triangle
     */
    @Override
    public String draw() {
        StringBuilder pic = new StringBuilder();
        String separator = System.lineSeparator();

        pic.append("  *  ");
        pic.append(separator);
        pic.append(" *** ");
        pic.append(separator);
        pic.append("*****");

        return pic.toString();
    }
}
