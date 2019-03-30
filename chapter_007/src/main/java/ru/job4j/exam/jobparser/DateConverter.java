package ru.job4j.exam.jobparser;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class DateConverter {
    private final static String yesterday = "вчера,";
    private final static String today = "сегодня,";

    public static LocalDateTime convertStringToLocalDateTime(String date) {
        validate(date);
        String dateToParse = "";
        LocalDateTime result = null;
        if (date.toLowerCase().strip().startsWith(yesterday)) {
            return parseStringDay(date, yesterday);
        } else if (date.toLowerCase().strip().startsWith(today)) {
            return parseStringDay(date, today);
        }
        int dayIsGreater9 = 15;
        DateTimeFormatter formatter = date.toLowerCase().strip().length() == dayIsGreater9
                ? DateTimeFormatter.ofPattern("dd MMM yy, hh:mm")
                : DateTimeFormatter.ofPattern("d MMM yy, hh:mm");
        return LocalDateTime.parse(date, formatter);
    }

    private static LocalDateTime parseStringDay(String date, String day) {
        String todayParsed = date.replaceFirst(day, "");
        LocalTime todayTime = LocalTime.parse(todayParsed,
                DateTimeFormatter.ofPattern("hh:mm"));
        int dayOff = yesterday.equals(day) ? 1 : 0;
        return LocalDateTime.of(LocalDate.now().minusDays(dayOff), todayTime);
    }

    public static Timestamp convertLocalDateTimeToTimestamp(LocalDateTime dateTime) {
        validate(dateTime);
        return Timestamp.valueOf(dateTime);
    }

    public static LocalDateTime convertTimestampToLocalDateTime(Timestamp timestamp) {
        validate(timestamp);
        return timestamp.toLocalDateTime();
    }

    private static void validate(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
    }
}
