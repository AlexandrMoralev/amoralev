package ru.job4j.condition;

/**
 * Point
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */

public class Point {

    private int x;
    private int y;
    /**
     * PointConstructor
     * @param x - "x" coordinate of the point.
     * @param y - "y" coordinate of the point.
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    /**
     * Distance.
     * @param that - other Point
     * @return -1 - some hardcode
     *
     */
    public double distanceTo(Point that) {
        return Math.sqrt(Math.pow(that.x - this.x, 2) + Math.pow(that.y - this.y, 2));
    }
    /**
     * Main.
     * @param args - args.
     */
    public static void main(String[] args) {
        Point p1 = new Point(1, 3);
        Point p2 = new Point(7, 9);
        System.out.println("x1 = " + p1.x);
        System.out.println("y1 = " + p1.y);
        System.out.println("x2 = " + p2.x);
        System.out.println("y2 = " + p2.y);

        double result = p1.distanceTo(p2);
        System.out.println("Расстояние между точками р1 и р2: " + result);
    }
}
