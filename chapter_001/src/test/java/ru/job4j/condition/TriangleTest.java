package ru.job4j.condition;


import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.IsCloseTo.closeTo;

/**
 * TriangleTest.
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class TriangleTest {

    /**
     * Test of creating triangle from 3 points and calculate it's area
     */
    @Test
    public void whenAreaSetThreePointsThenTriangleArea() {
        /* creating three Point objects. */
        Point firstPoint = new Point(0, 0);
        Point secondPoint = new Point(0, 2);
        Point thirdPoint = new Point(2, 0);
        /*creating new Triangle from three Points.*/
        Triangle triangle = new Triangle(firstPoint, secondPoint, thirdPoint);

        double result = triangle.area(); // calculating triangle area
        double expected = 2d; // expected result

        assertThat(result, closeTo(expected, 0.1)); // checking result and expected value
    }
}
