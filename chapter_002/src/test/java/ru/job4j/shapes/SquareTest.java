package ru.job4j.shapes;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * SquareTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class SquareTest {
    /**
     * Test. DrawSquare
     */
    @Test
    public void whenDrawSquare() {
        Square square = new Square();
        assertThat(
                square.draw(),
                is(
                        new StringBuilder()
                                .append("4444")
                                .append(System.lineSeparator())
                                .append("1  1")
                                .append(System.lineSeparator())
                                .append("1  1")
                                .append(System.lineSeparator())
                                .append("4444")
                                .toString()
                )
        );
    }
}
