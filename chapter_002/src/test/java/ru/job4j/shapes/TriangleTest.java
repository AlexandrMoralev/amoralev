package ru.job4j.shapes;

import org.junit.Test;

import static org.hamcrest.core.Is.*;
import static org.junit.Assert.*;

public class TriangleTest {
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
