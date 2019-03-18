package ru.job4j.exam.jobparser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Config - wrapper for a Properties
 * transfers DB settings to DBStore
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Config {
    //TODO add logger
    private final Properties properties = new Properties();

    public void init() {
        final String fileName = "jobparser_app.properties";
        try (InputStream in = Config.class
                .getClassLoader()
                .getResourceAsStream(fileName)
        ) {
            properties.load(in);
        } catch (IOException e) {
            //TODO add logging
            e.printStackTrace();
        }
    }

    public String get(String key) {
        return this.properties.getProperty(key);
    }
}
