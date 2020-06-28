package ru.job4j.impl;

import org.junit.jupiter.api.Test;
import ru.job4j.common.NamedArgs;
import ru.job4j.impl.search.FileSearch;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileSearchTest {

    private static final String SOURCE_PATH = "./data/search/";

// ByDefault

    @Test
    public void testSearchByDefault() throws IOException {
        NamedArgs namedArgs = NamedArgs.of(new String[]{"-d", SOURCE_PATH, "-n", "*.txt", "-default", "-o", "log.txt"});

        List<String> files = new FileSearch().findFiles(namedArgs).stream().map(Path::toString).collect(Collectors.toList());

        assertTrue(files.isEmpty());

    }

//ByFilename

    @Test
    public void testSearchByFilename() throws IOException {
        NamedArgs namedArgs = NamedArgs.of(new String[]{"-d", SOURCE_PATH, "-n", "file.txt", "-f", "-o", "log.txt"});

        List<String> files = new FileSearch().findFiles(namedArgs).stream().map(Path::toString).collect(Collectors.toList());

        assertEquals(2, files.size());
        assertTrue(files.contains(".\\data\\search\\file.txt"));
        assertTrue(files.contains(".\\data\\search\\subdirectory\\file.txt"));
    }

// SearchByWildcard

    @Test
    public void testSearchByMaskWithSubdirs() throws IOException {
        NamedArgs namedArgs = NamedArgs.of(new String[]{"-d", SOURCE_PATH, "-n", "*.txt", "-m", "-o", "log.txt"});

        List<String> files = new FileSearch().findFiles(namedArgs).stream().map(Path::toString).collect(Collectors.toList());

        assertEquals(3, files.size());
        assertTrue(files.contains(".\\data\\search\\file.txt"));
        assertTrue(files.contains(".\\data\\search\\subdirectory\\file.txt"));
        assertTrue(files.contains(".\\data\\search\\subdirectory\\subdir_file.txt"));

    }

    @Test
    public void testSearchByMask() throws IOException {
        NamedArgs namedArgs = NamedArgs.of(new String[]{"-d", SOURCE_PATH, "-n", "*.*", "-m", "-o", "log.txt"});

        List<String> files = new FileSearch().findFiles(namedArgs).stream().map(Path::toString).collect(Collectors.toList());

        assertEquals(6, files.size());
        assertTrue(files.contains(".\\data\\search\\file.txt"));
        assertTrue(files.contains(".\\data\\search\\file.html"));
        assertTrue(files.contains(".\\data\\search\\subdirectory\\file.txt"));
        assertTrue(files.contains(".\\data\\search\\subdirectory\\file.html"));
        assertTrue(files.contains(".\\data\\search\\subdirectory\\subdir_file.txt"));
        assertTrue(files.contains(".\\data\\search\\subdirectory\\subdir_file.html"));

    }

//SearchByRegexp

    @Test
    public void testSearchByRegexp() throws IOException {
        NamedArgs namedArgs = NamedArgs.of(new String[]{"-d", SOURCE_PATH, "-n", "([^\\s]+(\\.(?i)(txt|html))$)", "-r", "-o", "log.txt"});

        List<String> files = new FileSearch().findFiles(namedArgs).stream().map(Path::toString).collect(Collectors.toList());

        assertEquals(6, files.size());
        assertTrue(files.contains(".\\data\\search\\file.txt"));
        assertTrue(files.contains(".\\data\\search\\file.html"));
        assertTrue(files.contains(".\\data\\search\\subdirectory\\file.txt"));
        assertTrue(files.contains(".\\data\\search\\subdirectory\\file.html"));
        assertTrue(files.contains(".\\data\\search\\subdirectory\\subdir_file.txt"));
        assertTrue(files.contains(".\\data\\search\\subdirectory\\subdir_file.html"));
    }

}
