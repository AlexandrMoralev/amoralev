package ru.job4j.shapes;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PaintTest {
    @Test
    public void whenDrawSquare() {
        // get a reference to the standard console output
        PrintStream stdOut = System.out;
        // creating a buffer to store the output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // replacing standard output to buffer output for tests
        System.setOut(new PrintStream(outputStream));
        // executing console output methods
        new Paint().draw(new Square());
        // checking the result
        assertThat(
                new String(outputStream.toByteArray()),
                is(
                        new StringBuilder()
                                .append("4444")
                                .append("\r\n")
                                .append("1  1")
                                .append("\r\n")
                                .append("1  1")
                                .append("\r\n")
                                .append("4444")
                                .append(System.lineSeparator())
                                .append(System.lineSeparator())
                                .toString()
                )
        );
        // replacing buffer output back to standard output
        System.setOut(stdOut);
    }
}
