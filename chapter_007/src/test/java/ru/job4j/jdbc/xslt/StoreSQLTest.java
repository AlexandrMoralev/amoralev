package ru.job4j.jdbc.xslt;

import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * StoreSQLTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class StoreSQLTest {

    @Test
    public void whenGenerateThreeItemsShouldReturnListOfThreeItems() throws SQLException {
        final StoreSQL storeSQL = new StoreSQL(new Config());
        storeSQL.generate(3);
        final List<Entry> result = storeSQL.load();
        assertThat(result.get(0).getField(), is(1));
        assertThat(result.get(1).getField(), is(2));
        assertThat(result.get(2).getField(), is(3));
        assertThat(result.size(), is(3));
    }

    @Test
    public void whenGenerateZeroItemsShouldReturnEmptyList() throws SQLException {
        final StoreSQL storeSQL = new StoreSQL(new Config());
        storeSQL.generate(0);
        final List<Entry> result = storeSQL.load();
        assertThat(result.isEmpty(), is(true));
    }
}
