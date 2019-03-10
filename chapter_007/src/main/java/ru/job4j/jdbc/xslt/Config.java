package ru.job4j.jdbc.xslt;

import java.io.InputStream;
import java.util.Properties;

/**
 * Config - wrapper for a Properties
 * transfers SQLite settings to Store StoreSQL
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Config {
    private final Properties values = new Properties();

    public void init() {
        try (InputStream in = Config.class
                .getClassLoader()
                .getResourceAsStream("app_sqlite.properties")
        ) {
            values.load(in);
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }

    public String get(String key) {
        return this.values.getProperty(key);
    }
}
