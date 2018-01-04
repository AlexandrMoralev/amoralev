package ru.job4j.condition;

/**
 * Triangle
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */

public class Triangle {
    private Point a;
    private Point b;
    private Point c;

    /**
     * Triangle constructor
     * @param a first Point.
     * @param b second Point.
     * @param c third Point.
     */
    public Triangle(Point a, Point b, Point c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    /**
     * Method period.
     * @param ab int number
     * @param bc int number
     * @param ac int number
     * @return half-perimeter of the triangle
     */
    public double period(double ab, double bc, double ac) {
        return (ab + bc + ac)/2;
    }

    /**
     * Method area.
     * @return area of the triangle if it exists, or -1 if it doesn't
     */
    public double area() {
        double rsl = -1;

        double ab = this.a.distanceTo(this.b);
        double ac = this.a.distanceTo(this.c);
        double bc = this.b.distanceTo(this.c);
        double p = this.period(ab, ac, bc);

        if (this.exist(ab, ac, bc)) {
            rsl = Math.sqrt(p*(p-ab)*(p-ac)*(p-bc));
        }
        return rsl;
    }

    /**
     * Method exist.
     * @param ab int number
     * @param bc int number
     * @param ac int number
     * @return true if triangle exists, false if it doesn't
     */
    public boolean exist(double ab, double bc, double ac) {
        boolean result = false;

        if ((ab + bc > ac) & (ab + ac > bc) & (bc + ac > ab)) {
            result = true;
        }
        return result;
    }
}
