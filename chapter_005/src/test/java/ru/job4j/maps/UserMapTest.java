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
    private Object[] result;

    @Before
    public void init() {
        this.map = new UserMap();
        this.first = new User("Andrey",
                0,
                new GregorianCalendar(2008, 6, 1));

        this.second = new User("Andrey",
                0,
                new GregorianCalendar(2008, 6, 1));
        map.add(first);
        map.add(second);
        result = map.getMap().keySet().toArray();
        for (Object user : result) {
            System.out.println(user);
        }
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

    @Test
    public void whenOverrideOnlyEquals() {
        assertThat(result.length, is(2));
    }

    @Ignore
    @Test
    public void whenOverrideEqualsAndHashcode() {
        assertThat(result.length, is(1));
    }
}
