package ru.job4j.shapes;

import org.junit.Test;

import static org.hamcrest.core.Is.*;
import static org.junit.Assert.*;

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
                                .append("\r\n")
                                .append(" *** ")
                                .append("\r\n")
                                .append("*****")
                                .append("\r\n")
                                .toString()
                )
        );
    }
}
