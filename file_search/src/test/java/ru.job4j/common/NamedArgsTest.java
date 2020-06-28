package ru.job4j.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static ru.job4j.common.Constants.*;

public class NamedArgsTest {

    @Test
    public void whenSearchByMaskThenArgsIsValid() {
        NamedArgs namedArgs = NamedArgs.of(new String[]{"-d", "c:/", "-n", "*.txt", "-m", "-o", "log.txt"});

        assertEquals(namedArgs.getSearchCriteria(), SEARCH_BY_MASK);
        assertEquals(namedArgs.get(START_DIRECTORY), "c:/");
        assertEquals(namedArgs.get(SEARCH_CRITERIA), "*.txt");
        assertEquals(namedArgs.get(OUTPUT_FILEPATH), "log.txt");
    }

    @Test
    public void whenSearchByFilenameThenArgsIsValid() {
        NamedArgs namedArgs = NamedArgs.of(new String[]{"-d", "c:/", "-n", "*.txt", "-f", "-o", "log.txt"});

        assertEquals(namedArgs.getSearchCriteria(), SEARCH_BY_FILENAME);
        assertEquals(namedArgs.get(START_DIRECTORY), "c:/");
        assertEquals(namedArgs.get(SEARCH_CRITERIA), "*.txt");
        assertEquals(namedArgs.get(OUTPUT_FILEPATH), "log.txt");
    }

    @Test
    public void whenSearchByRegexpThenArgsIsValid() {
        NamedArgs namedArgs = NamedArgs.of(new String[]{"-d", "c:/", "-n", "*.txt", "-r", "-o", "log.txt"});

        assertEquals(namedArgs.getSearchCriteria(), SEARCH_BY_REGEXP);
        assertEquals(namedArgs.get(START_DIRECTORY), "c:/");
        assertEquals(namedArgs.get(SEARCH_CRITERIA), "*.txt");
        assertEquals(namedArgs.get(OUTPUT_FILEPATH), "log.txt");
    }

    @Test
    public void whenSearchByDefaultThenArgsIsValid() {
        NamedArgs namedArgs = NamedArgs.of(new String[]{"-d", "c:/", "-n", "*.txt", "-default", "-o", "log.txt"});

        assertEquals(namedArgs.getSearchCriteria(), SEARCH_BY_DEFAULT);
        assertEquals(namedArgs.get(START_DIRECTORY), "c:/");
        assertEquals(namedArgs.get(SEARCH_CRITERIA), "*.txt");
        assertEquals(namedArgs.get(OUTPUT_FILEPATH), "log.txt");
    }

    @Test
    public void whenGetNotExistingArg() {
        NamedArgs namedArgs = NamedArgs.of(new String[]{"-d", "c:/", "-n", "*.txt", "-m", "-o", "log.txt"});
        assertNull(namedArgs.get("Xmx"));
    }

    @Test
    public void whenNotEnoughArgs() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> NamedArgs.of(new String[]{"-d", "c:/", "-n", "-o", "log.txt"})
        );
        assertEquals(exception.getMessage(), Constants.TOOLTIP);
    }

    @Test
    public void whenNotEnoughMandatoryArgs() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> NamedArgs.of(new String[]{"-a", "c:/", "-b", "*.txt", "-c", "-q", "log.txt"})
        );
        assertTrue(exception.getMessage().contains("Not enough args."));
    }

    @Test
    public void whenNoSearchMethodInArgs() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> NamedArgs.of(new String[]{"-d", "c:/", "-n", "*.txt", "-k", "-o", "log.txt"})
        );
        assertEquals("No search method found", exception.getMessage());
    }

    @Test
    public void whenNoArgsExist() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> NamedArgs.of(new String[]{})
        );
        assertEquals(exception.getMessage(), Constants.TOOLTIP);
    }

}
