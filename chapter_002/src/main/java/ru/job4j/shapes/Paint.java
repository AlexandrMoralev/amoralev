package ru.job4j.shapes;

/**
 * Paint
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Paint {
    /**
     * Method draw - console output for shape drawing
     * @param shape some Shape to draw
     */
    public void draw(Shape shape) {
        System.out.println(shape.draw());
    }

    public static void main(String[] args) {
        Paint painter = new Paint();

        Triangle triangle = new Triangle();
        Square square = new Square();

        painter.draw(triangle);
        painter.draw(square);
    }
}
