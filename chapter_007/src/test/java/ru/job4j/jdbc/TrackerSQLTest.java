package ru.job4j.jdbc;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * TrackerSQLTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class TrackerSQLTest {
    @Test
    public void checkConnection() {
        TrackerSQL tracker = new TrackerSQL();
        assertThat(tracker.init(), is(true));
    }
}