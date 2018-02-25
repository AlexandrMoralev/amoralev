package ru.job4j.shapes;

/**
 * Square
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Square implements Shape {
    /**
     * Method draw - concat strings for drawing Square
     * @return String to draw square
     */
    @Override
    public String draw() {
        StringBuilder pic = new StringBuilder();
        String separator = System.lineSeparator();

        pic.append("4444");
        pic.append(separator);
        pic.append("1  1");
        pic.append(separator);
        pic.append("1  1");
        pic.append(separator);
        pic.append("4444");

        return pic.toString();
    }
}
