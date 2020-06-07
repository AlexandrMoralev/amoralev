package ru.job4j.inputoutput.serveravailabilityanalysis;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

public class Analysis {

    public void unavailable(String source, String target) {
        try (BufferedReader reader = new BufferedReader(new FileReader(source.strip()));
             BufferedWriter writer = new BufferedWriter(new FileWriter(target.strip()))
        ) {
            List<LogEntry> entries = parseLogEntries(reader);
            List<Period> unavailablePeriods = findUnavailablePeriods(entries);
            writer.write(
                    unavailablePeriods.stream()
                            .map(Period::toString)
                            .collect(Collectors.joining(System.lineSeparator()))
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<LogEntry> parseLogEntries(BufferedReader reader) {
        return reader.lines()
                .map(String::strip)
                .filter(not(String::isBlank))
                .map(LogEntry::fromLine)
                .flatMap(Optional::stream)
                .sorted(Comparator.comparing(LogEntry::getDateTime))
                .collect(Collectors.toList());
    }

    private List<Period> findUnavailablePeriods(List<LogEntry> entries) {
        List<Period> unavailablePeriods = new ArrayList<>();
        String start = null;
        String end = null;
        for (LogEntry entry : entries) {
            if (entry.getStatus() == StatusCode.BAD_REQUEST || entry.getStatus() == StatusCode.SERVER_ERROR) {
                if (start == null) {
                    start = entry.getDateTime();
                    continue;
                }
            }
            if (entry.getStatus() == StatusCode.OK || entry.getStatus() == StatusCode.REDIRECT) {
                end = entry.getDateTime();
                if (start != null) {
                    unavailablePeriods.add(new Period(start, end));
                    start = null;
                    end = null;
                }
            }
        }
        return unavailablePeriods;
    }

    public static void main(String[] args) {
        new Analysis().unavailable("server.log", "unavailable.csv");
    }
}
