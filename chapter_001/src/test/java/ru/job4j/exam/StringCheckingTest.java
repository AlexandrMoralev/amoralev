package ru.job4j.exam;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * StringCheckingTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class StringCheckingTest {

    /**
     * Test. Checking "ell" in "Hello"
     */
    @Test
    public void whenHelloContainsEllThenTrue() {
        StringChecking stringChecking = new StringChecking();
        assertThat(
                stringChecking.contains("Hello", "ell"),
                is(true)
        );
    }

    /**
     * Test. Checking "Fair" in "Hello"
     */
    @Test
    public void whenHelloNotContainsFairThenFalse() {
        StringChecking stringChecking = new StringChecking();
        assertThat(
                stringChecking.contains("Hello", "Fair"),
                is(false)
        );
    }

    /**
     * Test. Checking "Helo" in "Hello"
     */
    @Test
    public void whenHelloContainsHeloThenFalse() {
        StringChecking stringChecking = new StringChecking();
        assertThat(
                stringChecking.contains("Hello", "helo"),
                is(false)
        );
    }

    /**
     * Test. Checking "ellowsubmarine" in "Hello" (ArrayIndexOutOfBoundsException)
     */
    @Test
    public void whenHelloNotContainsEllowsubmarineThenFalse() {
        StringChecking stringChecking = new StringChecking();
        assertThat(
                stringChecking.contains("Hello", "ellowsubmarine"),
                is(false)
        );
    }
}
