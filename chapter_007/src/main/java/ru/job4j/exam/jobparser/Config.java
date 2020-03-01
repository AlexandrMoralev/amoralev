package ru.job4j.exam.jobparser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    private static final Logger LOG = LogManager.getLogger(Config.class);
    private final Properties properties;

    public Config(String propertiesFileName) {
        this.properties = new Properties();
        init(propertiesFileName);
    }
    public Config() {
        this("");
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
            LOG.error("Error loading properties", e);
            throw new RuntimeException("Unexpected error");
        }
    }

    public String getString(String key) {
        return this.properties.getProperty(key);
    }

    public Integer getInt(String key) {
        return Integer.valueOf(this.properties.getProperty(key).trim());
    }

}
