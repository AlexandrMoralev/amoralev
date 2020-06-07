package ru.job4j.inputoutput.serveravailabilityanalysis;

public class Period {
    private final String start;
    private final String end;

    public Period(String start, String end) {
        this.start = start;
        this.end = end;
    }

    public Period(String start) {
        this.start = start;
        this.end = "not finished";
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return String.format("%s;%s", start, end);
    }
}
