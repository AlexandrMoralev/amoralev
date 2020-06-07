package ru.job4j.inputoutput.config;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ConfigTest {

    private static final Map<String, String> EXPECTED = Map.of(
            "hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect",
            "hibernate.connection.url", "jdbc:postgresql://127.0.0.1:5432/trackstudio",
            "hibernate.connection.driver_class", "org.postgresql.Driver",
            "hibernate.connection.username", "postgres",
            "hibernate.connection.password", "password"
    );

    @Test
    public void whenPairWithoutComment() {
        String path = "./data/config/pairs_without_comment.properties";
        Config config = new Config(path);
        config.load();
        assertEquals(config.value("hibernate.dialect"), EXPECTED.get("hibernate.dialect"));
        assertEquals(config.value("hibernate.connection.url"), EXPECTED.get("hibernate.connection.url"));
        assertEquals(config.value("hibernate.connection.driver_class"), EXPECTED.get("hibernate.connection.driver_class"));
        assertEquals(config.value("hibernate.connection.username"), EXPECTED.get("hibernate.connection.username"));
        assertEquals(config.value("hibernate.connection.password"), EXPECTED.get("hibernate.connection.password"));

    }

    @Test
    public void whenPairWithComment() {
        String path = "./data/config/pairs_with_comment.properties";
        Config config = new Config(path);
        config.load();
        assertEquals(config.value("hibernate.dialect"), EXPECTED.get("hibernate.dialect"));
        assertEquals(config.value("hibernate.connection.url"), EXPECTED.get("hibernate.connection.url"));

        assertNull(config.value("hibernate.connection.driver_class"));

        assertEquals(config.value("hibernate.connection.username"), EXPECTED.get("hibernate.connection.username"));
        assertEquals(config.value("hibernate.connection.password"), EXPECTED.get("hibernate.connection.password"));
    }

    @Test
    public void whenPairWithEmptyLines() {
        String path = "./data/config/pairs_with_empty_lines.properties";
        Config config = new Config(path);
        config.load();
        assertEquals(config.value("hibernate.dialect"), EXPECTED.get("hibernate.dialect"));
        assertEquals(config.value("hibernate.connection.url"), EXPECTED.get("hibernate.connection.url"));
        assertEquals(config.value("hibernate.connection.driver_class"), EXPECTED.get("hibernate.connection.driver_class"));
        assertEquals(config.value("hibernate.connection.username"), EXPECTED.get("hibernate.connection.username"));
        assertEquals(config.value("hibernate.connection.password"), EXPECTED.get("hibernate.connection.password"));
    }

    @Test
    public void whenPairWithSpaces() {
        String path = "./data/config/pairs_with_spaces.properties";
        Config config = new Config(path);
        config.load();
        assertEquals(config.value("hibernate.dialect"), EXPECTED.get("hibernate.dialect"));
        assertEquals(config.value("hibernate.connection.url"), EXPECTED.get("hibernate.connection.url"));
        assertEquals(config.value("hibernate.connection.driver_class"), EXPECTED.get("hibernate.connection.driver_class"));
        assertEquals(config.value("hibernate.connection.username"), EXPECTED.get("hibernate.connection.username"));
        assertEquals(config.value("hibernate.connection.password"), EXPECTED.get("hibernate.connection.password"));
    }

    @Test
    public void whenPairIncorrect() {
        String path = "./data/config/incorrect_pairs.properties";
        Config config = new Config(path);
        config.load();
        assertEquals(config.value("hibernate.dialect"), EXPECTED.get("hibernate.dialect"));
        assertEquals(config.value("hibernate.connection.url"), EXPECTED.get("hibernate.connection.url"));
        assertEquals(config.value("hibernate.connection.driver_class"), EXPECTED.get("hibernate.connection.driver_class"));
        assertEquals(config.value("hibernate.connection.username"), EXPECTED.get("hibernate.connection.username"));
        assertEquals(config.value("hibernate.connection.password"), EXPECTED.get("hibernate.connection.password"));
        assertNull(config.value("hibernate"));
        assertNull(config.value(""));

    }

}
