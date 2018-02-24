package ru.job4j.shapes;

public class Paint {
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
