package ru.job4j.sorting;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * UserTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class UserTest {

    /**
     * Test. compareTo() > 0.
     */
    @Test
    public void whenFirstOlderThanSecondThenResultIsGreaterThan0() {
        User first = new User("Ivan", 25);
        User second = new User("Denis", 19);

        assertThat(first.compareTo(second) > 0, is(true));
    }

    /**
     * Test. compareTo() < 0.
     */
    @Test
    public void whenFirstYoungerThanSecondThenResultIsLessThan0() {
        User first = new User("Ivan", 1);
        User second = new User("Denis", 100);

        assertThat(first.compareTo(second) > 0, is(false));
    }

    /**
     * Test. compareTo() == 0.
     */
    @Test
    public void whenFirstAndSecondAreCoevalsThenResultIs0() {
        User first = new User("Ivan", 1);
        User second = new User("Denis", 1);

        assertThat(first.compareTo(second), is(0));
    }
}
