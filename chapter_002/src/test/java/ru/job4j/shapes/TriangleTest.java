package ru.job4j.shapes;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * TriangleTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class TriangleTest {
    /**
     * Test. Drawing Triangle.
     */
    @Test
    public void whenDrawTriangle() {
        Triangle triangle = new Triangle();
        assertThat(
                triangle.draw(),
                is(
                        new StringBuilder()
                                .append("  *  ")
                                .append(System.lineSeparator())
                                .append(" *** ")
                                .append(System.lineSeparator())
                                .append("*****")
                                .toString()
                )
        );
    }
}
