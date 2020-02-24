package ru.job4j.exam.jobparser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

import static java.util.Map.entry;

public final class DateConverter {

    private final static Logger LOG = LogManager.getLogger(DateConverter.class);

    private final static String YESTERDAY = "вчера,";
    private final static String TODAY = "сегодня,";
    private final static Map<String, String> MONTHS = Map.ofEntries(
            entry("янв", "Jan"),
            entry("фев", "Feb"),
            entry("мар", "Mar"),
            entry("апр", "Apr"),
            entry("май", "May"),
            entry("июн", "Jun"),
            entry("июл", "Jul"),
            entry("авг", "Aug"),
            entry("сен", "Sep"),
            entry("окт", "Oct"),
            entry("ноя", "Nov"),
            entry("дек", "Dec")
    );
  /*  private final static Map<String, String> monthsE = Map.ofEntries(
            entry("янв", "jan"),
            entry("фев", "feb"),
            entry("мар", "mar"),
            entry("апр", "apr"),
            entry("май", "may"),
            entry("июн", "jun"),
            entry("июл", "jul"),
            entry("авг", "aug"),
            entry("сен", "sep"),
            entry("окт", "oct"),
            entry("ноя", "nov"),
            entry("дек", "dec")
    );
    private final static Map<String, String> ruMonths = Map.ofEntries(
            entry("янв", "Янв"),
            entry("фев", "Фев"),
            entry("мар", "Мар"),
            entry("апр", "Апр"),
            entry("май", "Мая"),
            entry("июн", "Июн"),
            entry("июл", "Июл"),
            entry("авг", "Авг"),
            entry("сен", "Сен"),
            entry("окт", "Окт"),
            entry("ноя", "Ноя"),
            entry("дек", "Дек")
    );*/

    public static LocalDateTime convertStringToLocalDateTime(String date) {
 /*       validate(date);
        String newDate = date;
        if (date.toLowerCase().strip().startsWith(YESTERDAY)) {
            return parseStringDay(newDate, YESTERDAY);
        } else if (date.toLowerCase().strip().startsWith(TODAY)) {
            return parseStringDay(newDate, TODAY);
        }
     //   newDate = replaceMonth(date);
        newDate = date;
        int dayIsGreater9 = 15;
        DateTimeFormatter formatter = newDate.toLowerCase().strip().length() == dayIsGreater9
                ? DateTimeFormatter.ofPattern("dd MMM yy, hh:mm")
                : DateTimeFormatter.ofPattern("d MMM yy, hh:mm");
        return LocalDateTime.parse(newDate, formatter.withLocale( new Locale("ru_RU"))); // .withLocale( new Locale("ru"))
           */

        validate(date);
        String newDate = date;
        if (date.toLowerCase().trim().startsWith(YESTERDAY)) {
            return parseStringDay(newDate, YESTERDAY);
        } else if (date.toLowerCase().trim().startsWith(TODAY)) {
            return parseStringDay(newDate, TODAY);
        }
        newDate = replaceMonth(date);
        int dayIsGreater9 = 15;
        DateTimeFormatter formatter = newDate.toLowerCase().trim().length() == dayIsGreater9
                ? DateTimeFormatter.ofPattern("dd MMM yy, hh:mm")
                : DateTimeFormatter.ofPattern("d MMM yy, hh:mm");
        formatter = formatter.withLocale(Locale.ENGLISH);
        LocalDateTime result = LocalDateTime.parse(newDate, formatter);
        return result; // .withLocale( new Locale("ru")) .withLocale(new Locale("ru"))
    }

    public static String replaceMonth(String date) {
        String source = date.toLowerCase().strip();
        for (String month : MONTHS.keySet()) {
            if (source.contains(month)) {
                source = source.replace(month, MONTHS.get(month));
            }
        }
        return source;
    }

    private static LocalDateTime parseStringDay(String date, String day) {
        String todayParsed = date.replaceFirst(day, "");
        LocalTime todayTime = LocalTime.parse(todayParsed.trim(),
                DateTimeFormatter.ofPattern("HH:mm"));
        long dayOff = YESTERDAY.equals(day) ? 1L : 0L;
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

    public static Timestamp convertStringDateToTimestamp(String date) {
        validate(date);
        return convertLocalDateTimeToTimestamp(convertStringToLocalDateTime(date));
    }

    private static void validate(Object o) {
        if (o == null) {
            LOG.error("Validation error");
            throw new NullPointerException();
        }
    }
}
