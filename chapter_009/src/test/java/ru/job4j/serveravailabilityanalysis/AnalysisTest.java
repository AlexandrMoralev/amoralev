package ru.job4j.serveravailabilityanalysis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.job4j.inputoutput.serveravailabilityanalysis.Analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnalysisTest {

    private static final String SOURCE_PATH = "./data/serverlogs/";
    private static final String TARGET_PATH = "unavailable.csv";
    private Analysis analysis;

    @TempDir
    File source;

    @TempDir
    File target;

    @BeforeEach
    public void init() throws IOException {
        analysis = new Analysis();
        assertTrue(this.source.isDirectory());
        assertTrue(this.target.isDirectory());
    }

    @Test
    public void whenLogWithUnavailablePeriods() throws IOException {

        File input = new File(SOURCE_PATH, "log_with_unavailable_periods.log");
        File output = new File(target, TARGET_PATH);

        Set<String> expected = Set.of(
                "10:57:01;10:59:01",
                "11:01:02;11:02:02"
        );

        analysis.unavailable(input.getAbsolutePath(), output.getAbsolutePath());

        try (BufferedReader reader = new BufferedReader(new FileReader(output))) {
            assertTrue(reader.lines().allMatch(expected::contains));
        }
    }

    @Test
    public void whenLogWithoutUnavailablePeriods() throws IOException {

        File input = new File(SOURCE_PATH, "log_without_unavailable_periods.log");
        File output = new File(target, TARGET_PATH);

        analysis.unavailable(input.getAbsolutePath(), output.getAbsolutePath());

        try (BufferedReader reader = new BufferedReader(new FileReader(output))) {
            assertTrue(reader.lines().collect(Collectors.toList()).isEmpty());
        }
    }

    @Test
    public void whenLogIsEmpty() throws IOException {

        File input = new File(SOURCE_PATH, "log_empty.log");
        File output = new File(target, TARGET_PATH);

        analysis.unavailable(input.getAbsolutePath(), output.getAbsolutePath());

        try (BufferedReader reader = new BufferedReader(new FileReader(output))) {
            assertTrue(reader.lines().collect(Collectors.toList()).isEmpty());
        }
    }

    @Test
    public void whenLogWithOnlyErrors() throws IOException {

        File input = new File(SOURCE_PATH, "log_with_only_errors.log");
        File output = new File(target, TARGET_PATH);

        analysis.unavailable(input.getAbsolutePath(), output.getAbsolutePath());

        try (BufferedReader reader = new BufferedReader(new FileReader(output))) {
            assertTrue(reader.lines().collect(Collectors.toList()).isEmpty());
        }
    }

}
