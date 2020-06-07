package ru.job4j.inputoutput.serveravailabilityanalysis;

import java.util.Optional;

public class LogEntry {

    private final StatusCode status;
    private final String dateTime;

    public LogEntry(StatusCode status, String dateTime) {
        this.status = status;
        this.dateTime = dateTime;
    }

    public StatusCode getStatus() {
        return status;
    }

    public String getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LogEntry{");
        sb.append("status=").append(status);
        sb.append(", dateTime='").append(dateTime).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static Optional<LogEntry> fromLine(String line) {
        String[] pair = line.strip().split("\\s+");
        if (pair.length != 2 || pair[0].strip().isBlank() || pair[1].strip().isBlank()) {
            return Optional.empty();
        }
        return StatusCode.valueOfCode(pair[0].strip())
                .map(code -> new LogEntry(code, pair[1].strip()));
    }
}
