package ru.job4j.maps;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.GregorianCalendar;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * UserMapTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class UserMapTest {

    private UserMap map;
    private User first;
    private User second;
    private User third;
    private User fourth;
    private Object[] result;

    @Before
    public void init() {
        this.map = new UserMap();
        this.first = new User("Andrey",
                0,
                new GregorianCalendar(2008, 6, 1)
        );

        this.second = new User("Andrey",
                0,
                new GregorianCalendar(2008, 6, 1)
        );
        map.add(first);
        map.add(second);
        result = map.getMap().keySet().toArray();
    }

    @Ignore
    @Test
    public void whenDoesntOverrideEqualsAndHashcode() {
        assertThat(result.length, is(2));
    }

    @Ignore
    @Test
    public void whenOverrideOnlyHashcode() {
        assertThat(result.length, is(2));
    }

    @Ignore
    @Test
    public void whenOverrideOnlyEquals() {
        assertThat(result.length, is(2));
    }

    @Test
    public void whenOverrideEqualsAndHashcode() {
        assertThat(result.length, is(1));
    }

    @Test
    public void equalsReflexivityTest() {
        assertThat(first.equals(first), is(true));
        assertThat(second.equals(second), is(true));
    }

    @Test
    public void equalsSymmetryTest() {
        assertThat(first.equals(second), is(true));
        assertThat(second.equals(first), is(true));
    }

    @Test
    public void equalsTransitivityTest() {
        third = new User("Andrey",
                0,
                new GregorianCalendar(2008, 6, 1)
        );
        assertThat(first.equals(second), is(true));
        assertThat(second.equals(third), is(true));
        assertThat(third.equals(first), is(true));
    }

    @Test
    public void equalsConsistencyTest() {
        assertThat(first.equals(second), is(true));
        third = first;
        fourth = second;
        assertThat(third.equals(first), is(true));
        assertThat(fourth.equals(second), is(true));
        assertThat(third.equals(fourth), is(true));
    }

    @Test
    public void whenEqualsForNullReferenceThenReturnsFalse() {
        assertThat(first.equals(null), is(false));
        assertThat(second.equals(null), is(false));
    }
}
