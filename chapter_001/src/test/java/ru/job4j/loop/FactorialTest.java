package ru.job4j.loop;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * FactorialTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */

public class FactorialTest {

    /**
     * Test. 5! == 120
     */
    @Test
    public void whenNumberEqualsFiveThen120() {
        Factorial f = new Factorial();
        int result = f.calc(5);
        assertThat(result, is(120));
    }

    /**
     * Test. 0! == 1
     */
    @Test
    public void whenNumberIsZeroThenOne() {
        Factorial f = new Factorial();
        int result = f.calc(0);
        assertThat(result, is(1));
    }
}
