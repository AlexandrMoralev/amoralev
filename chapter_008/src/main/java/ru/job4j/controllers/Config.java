package ru.job4j.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.Properties;

/**
 * Config - wrapper for a Properties
 * transfers app settings to db
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Config {
    private static final Logger LOG = LogManager.getLogger(Config.class);
    private final Properties values = new Properties();


    public Config(String propertiesFileName) {
        init(propertiesFileName);
    }

    public Config() {
        this("");
    }

    private void init(String propertiesFileName) {
        String defaultPropsFileName = "users_app.properties";
        String fileName = (propertiesFileName == null || propertiesFileName.isBlank())
                ? defaultPropsFileName
                : propertiesFileName;
        try (InputStream in = Config.class
                .getClassLoader()
                .getResourceAsStream(fileName)
        ) {
            values.load(in);
        } catch (Exception e) {
            LOG.error("Error loading properties", e);
            throw new RuntimeException("Unexpected error");
        }
    }

    public String getString(String key) {
        return this.values.getProperty(key).trim();
    }

    public Integer getInt(String key) {
        return Integer.valueOf(this.values.getProperty(key).trim());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Config{ ");
        sb.append("properties=").append(values);
        sb.append('}');
        return sb.toString();
    }
}
