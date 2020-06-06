package ru.job4j.exam;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
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
     * Test. consistencyHashcodeTest
     */
    @Test
    public void consistencyHashcodeTest() {
        User first = new User("Alex", "12345a");
        assertThat(first.hashCode() == first.hashCode()
                        & first.hashCode() == first.hashCode(),
                is(true)
        );
    }

    /**
     * Test. equalUsersHasEqualHash
     */
    @Test
    public void equalUsersHasEqualHash() {
        User first = new User("Alex", "12345a");
        User second = new User("Alex", "12345a");
        assertThat(first.hashCode() == second.hashCode(), is(true));
    }

    /**
     * Test. differentUsersHasDifferentHash
     */
    @Test
    public void differentUsersHasDifferentHash() {
        User first = new User("Alex", null);
        User second = new User("alex", null);
        assertThat(first.hashCode() != second.hashCode(), is(true));
    }

    /**
     * Test. reflexiveEqualsTest
     */
    @Test
    public void reflexiveEqualsTest() {
        User first = new User("Alex", "12345a");
        assertThat(first.equals(first), is(true));
    }

    /**
     * Test. symmetricEqualsTest
     */
    @Test
    public void symmetricEqualsTest() {
        User first = new User("Alex", "12345a");
        User second = new User("Alex", "12345a");
        assertThat(first.equals(second) & second.equals(first), is(true));
    }

    /**
     * Test. transitiveEqualsTest
     */
    @Test
    public void transitiveEqualsTest() {
        User first = new User("Alex", "12345a");
        User second = new User("Alex", "12345a");
        User third = new User("Alex", "12345a");
        assertThat(first.equals(second)
                        & second.equals(third)
                        & third.equals(first),
                is(true)
        );
    }

    /**
     * Test. differingEqualsTest
     */
    @Test
    public void differingEqualsTest() {
        User first = new User("Alex", "12345a");
        User second = new User("alex", "12345a");
        assertThat(first.equals(second), is(false));
    }

    /**
     * Test. hashcodeEqualityTest
     */
    @Test
    public void hashcodeEqualityTest() {
        User first = new User("Alex", "12345a");
        User second = new User("Alex", "12345a");
        assertThat(first.equals(second)
                        && first.hashCode() == second.hashCode(),
                is(true)
        );
    }
}
