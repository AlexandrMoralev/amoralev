package ru.job4j.tracker;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * ValidateInputTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class ValidateInputTest {

    // "buffered" out reference
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    // std out reference
    private final PrintStream stdOut = System.out;

    /**
     * Method loadOut - replaces stdOut with "buffered" for tests
     */
    @Before
    public void loadOut() {
        System.setOut(new PrintStream(this.out));
    }

    /**
     * Method loadStdOut - replaces "buffered" out with stdOut back
     */
    @After
    public void loadStdOut() {
        System.setOut(this.stdOut);
    }

    /**
     * Test. When invalid input then printing exception msg
     */
    @Test
    public void whenInvalidInput() {
        ValidateInput input = new ValidateInput(
                new StubInput(new String[] {"invalidData", "1"})
        );
        input.ask("Enter", new int[] {1});
        assertThat(
                this.out.toString(),
                is(
                        String.format("Please enter valid data. %n")
                )
        );
    }
}
