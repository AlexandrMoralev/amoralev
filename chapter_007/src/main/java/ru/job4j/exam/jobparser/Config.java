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
    private final Properties properties;

    public Config(String propertiesFileName) {
        this.properties = new Properties();
        init(propertiesFileName);
    }

    private void init(String propertiesFileName) {
        String defaultPropsFileName = "jobparser_app.properties";
        String fileName = (propertiesFileName == null || propertiesFileName.isBlank())
                ? defaultPropsFileName
                : propertiesFileName;
        try (InputStream in = Config.class
                .getClassLoader()
                .getResourceAsStream(fileName)
        ) {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace(); //TODO add logging
        }
    }

    public String get(String key) {
        return this.properties.getProperty(key);
    }
}
