package ru.job4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

/**
 * Config - wrapper for a Properties
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public enum Config {
    INSTANCE;

    private static final Logger LOG = LogManager.getLogger(Config.class);
    private final Properties properties;

    Config(String propertiesFileName) {
        this.properties = new Properties();
        loadFromFile(propertiesFileName);
        checkMandatoryFields();
    }

    Config() {
        this(null);
    }

    private void loadFromFile(String propertiesFileName) {
        String defaultPropsFileName = "cinema_app.properties";
        String fileName = (propertiesFileName == null || propertiesFileName.isBlank())
                ? defaultPropsFileName
                : propertiesFileName;

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            if (classLoader == null) {
                throw new RuntimeException("");
            }
            InputStream inputStream = classLoader.getResourceAsStream(fileName);
            if (inputStream == null) {
                throw new RuntimeException("Error reading properties file.");
            }
            properties.load(inputStream);
        } catch (IOException | NullPointerException e) {
            LOG.error("Error loading properties", e);
            throw new RuntimeException("Unexpected error", e);
        }
    }

    private void checkMandatoryFields() {
        Stream.of(
                "cinema.db.connection.url",
                "cinema.db.user",
                "cinema.db.pwd",
                "cinema.db.driver",
                "hall.size"
        )
                .filter(key -> !properties.containsKey(key))
                .findAny()
                .ifPresent(s -> {
                    String message = String.format("Property: %s not found", s);
                    LOG.error(message);
                    throw new RuntimeException(message);
                });
    }

    public Optional<String> getString(String key) {
        return ofNullable(this.properties.getProperty(key));
    }

    public Optional<Integer> getInt(String key) {
        return ofNullable(this.properties.getProperty(key)).map(value -> Integer.valueOf(value.trim()));
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Config{ ");
        sb.append("properties=").append(properties);
        sb.append('}');
        return sb.toString();
    }

}
