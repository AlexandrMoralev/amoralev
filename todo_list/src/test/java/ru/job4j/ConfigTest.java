package ru.job4j;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.exception.CommonException;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigTest {

    private static Config config;

    @BeforeAll
    public static void init() {
        config = new Config("test.properties");
    }

    @Test
    public void testGetStringProperty() {
        assertEquals("test.hibernate.cfg.xml", config.getString("hibernate.config.filename"));
    }

    @Test
    public void testGetIntProperty() {
        assertEquals(10, config.getInt("cache.cleanup.period.seconds"));
    }

    @Test
    public void testGetFloatProperty() {
        assertEquals(0, Float.compare(0.6f, config.getFloat("cache.load.factor")));
    }

    @Test
    public void testEmptyPropertyShouldThrowException() {
        Exception exception = assertThrows(CommonException.class,
                () -> config.getString("empty.property")
        );
        assertTrue(exception.getMessage().contains("Property with key: empty.property not found"));
    }
}
