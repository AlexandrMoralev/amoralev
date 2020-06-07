package ru.job4j.serveravailabilityanalysis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.inputoutput.serveravailabilityanalysis.Analysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnalysisTest {

    private static final String TARGET_PATH = "unavailable.csv";
    private Analysis analysis;

    @BeforeEach
    public void init() {
        analysis = new Analysis();
    }

    @Test
    public void whenLogWithUnavailablePeriods() throws IOException {
        String source = "./data/serverlogs/log_with_unavailable_periods.log";
        Set<String> expected = Set.of(
                "10:57:01;10:59:01",
                "11:01:02;11:02:02"
        );
        analysis.unavailable(source, TARGET_PATH);

        try (BufferedReader reader = new BufferedReader(new FileReader(TARGET_PATH))) {
            assertTrue(reader.lines().allMatch(expected::contains));
        }
    }

    @Test
    public void whenLogWithoutUnavailablePeriods() throws IOException {
        String source = "./data/serverlogs/log_without_unavailable_periods.log";

        analysis.unavailable(source, TARGET_PATH);

        try (BufferedReader reader = new BufferedReader(new FileReader(TARGET_PATH))) {
            assertTrue(reader.lines().collect(Collectors.toList()).isEmpty());
        }
    }

    @Test
    public void whenLogIsEmpty() throws IOException {
        String source = "./data/serverlogs/log_empty.log";

        analysis.unavailable(source, TARGET_PATH);

        try (BufferedReader reader = new BufferedReader(new FileReader(TARGET_PATH))) {
            assertTrue(reader.lines().collect(Collectors.toList()).isEmpty());
        }
    }

    @Test
    public void whenLogWithOnlyErrors() throws IOException {
        String source = "./data/serverlogs/log_with_only_errors.log";

        analysis.unavailable(source, TARGET_PATH);

        try (BufferedReader reader = new BufferedReader(new FileReader(TARGET_PATH))) {
            assertTrue(reader.lines().collect(Collectors.toList()).isEmpty());
        }
    }

}
