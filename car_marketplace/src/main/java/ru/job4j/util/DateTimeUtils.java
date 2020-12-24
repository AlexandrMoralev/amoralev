package ru.job4j.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * DateTimeUtils
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public final class DateTimeUtils {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"; // TODO add several formats and parseSafely()
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

    public static LocalDateTime convertToLocalDateTime(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER);
    }

    public static String convertToString(LocalDateTime localDateTime) {
        return DATE_TIME_FORMATTER.format(localDateTime);
    }

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

}
