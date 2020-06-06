package ru.job4j.shapes;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.StringJoiner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * PaintTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class PaintTest {

    // reference to the standard console output
    private final PrintStream stdOut = System.out;

    // buffer to store the output
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    /**
     * Method loadOutput replaces standard output to buffer output for tests
     */
    @BeforeEach
    public void loadOutput() {
        System.out.println("execute before method");
        System.setOut(new PrintStream(this.outputStream));
    }

    /**
     * Method backOutput replaces buffer output back to standard output
     */
    @AfterEach
    public void backOutput() {
        System.setOut(this.stdOut);
        System.out.println("execute after method");
    }

    /**
     * Test. Painting Square.
     */
    @Test
    public void whenDrawSquare() {
        // executing console output methods
        new Paint().draw(new Square());
        // checking the result
        assertThat(
                new String(this.outputStream.toByteArray()),
                is(
                        new StringBuilder()
                                .append("4444")
                                .append(System.lineSeparator())
                                .append("1  1")
                                .append(System.lineSeparator())
                                .append("1  1")
                                .append(System.lineSeparator())
                                .append("4444")
                                .append(System.lineSeparator())
                                .toString()
                )
        );
    }

    /**
     * Test. Painting Triangle.
     */
    @Test
    public void whenDrawTriangle() {
        new Paint().draw(new Triangle());
        assertThat(
                new String(this.outputStream.toByteArray()),
                is(
                        new StringJoiner("")
                                .add("  *  ")
                                .add(System.lineSeparator())
                                .add(" *** ")
                                .add(System.lineSeparator())
                                .add("*****")
                                .add(System.lineSeparator())
                                .toString()
                )
        );
    }
}
