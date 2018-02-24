package ru.job4j.shapes;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SquareTest {
    @Test
    public void whenDrawSquare() {
        Square square = new Square();
        assertThat(
                square.draw(),
                is(
                        new StringBuilder()
                                .append("4444")
                                .append("\r\n")
                                .append("1  1")
                                .append("\r\n")
                                .append("1  1")
                                .append("\r\n")
                                .append("4444")
                                .append("\r\n")
                                .toString()
                )
        );
    }
}
