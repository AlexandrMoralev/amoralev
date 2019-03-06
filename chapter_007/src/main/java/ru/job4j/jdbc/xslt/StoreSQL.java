package ru.job4j.jdbc.xslt;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;

/**
 * StoreSQL - генерит N записей в базе,
 *              передает List<Entry> в StoreXML
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class StoreSQL implements AutoCloseable {
    private final Config config;
    private Connection connect;

    public StoreSQL(Config config) {
        this.config = config;
    }

    /*    генерирует в базе данных n записей.
    описывается схемой */
    public void generate(int size) {
    /*        create table entry {
            field integer;
        }*/
    }

   public List<XmlUsage.Entry> load() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public void close() throws Exception {
        if (connect != null) {
            connect.close();
        }
    }
}
