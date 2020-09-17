package ru.job4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.exception.CommonException;

import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Supplier;

import static java.util.Optional.ofNullable;
import static java.util.function.Predicate.not;

/**
 * Config - wrapper for a Properties
 * transfers app settings
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Config {

    private static final Logger LOG = LogManager.getLogger(Config.class);
    protected final Properties values = new Properties();


    public Config(String propertiesFileName) {
        init(propertiesFileName);
    }

    public Config() {
        this("");
    }

    private void init(String propertiesFileName) {
        String defaultPropsFileName = "todo_app.properties";
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
        return getOptionalProperty(key)
                .orElseThrow(propertyNotFoundException(key));
    }

    public Integer getInt(String key) {
        return getOptionalProperty(key)
                .map(Integer::valueOf)
                .orElseThrow(propertyNotFoundException(key));
    }

    public Float getFloat(String key) {
        return getOptionalProperty(key)
                .map(Float::parseFloat)
                .orElseThrow(propertyNotFoundException(key));
    }

    protected Optional<String> getOptionalProperty(String key) {
        return ofNullable(this.values.getProperty(key))
                .map(String::strip)
                .filter(not(String::isBlank));
    }

    protected Supplier<CommonException> propertyNotFoundException(String key) {
        return () -> new CommonException(String.format("Property with key: %s not found", key));
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Config{ ");
        sb.append("properties=").append(values);
        sb.append('}');
        return sb.toString();
    }
}
