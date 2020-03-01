package ru.job4j.exam.jobparser;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Map.entry;

public final class DateTimeUtil {

    private final static DateTimeFormatter commonFormatter = DateTimeFormat.forPattern("dd MM yy, HH:mm");
    private final static String YESTERDAY = "вчера";
    private final static String TODAY = "сегодня";
    private final static String SPACE_SPLITTER = "\\s+";

    private final static Map<String, String> MONTHS = Map.ofEntries(
            entry("янв", "01"),
            entry("фев", "02"),
            entry("мар", "03"),
            entry("апр", "04"),
            entry("май", "05"),
            entry("июн", "06"),
            entry("июл", "07"),
            entry("авг", "08"),
            entry("сен", "09"),
            entry("окт", "10"),
            entry("ноя", "11"),
            entry("дек", "12")
    );

    public static LocalDateTime parseToLocalDateTime(String dateTimeString) {
        String dtString = dateTimeString.trim().toLowerCase();
        String[] splittedDateTime = dtString.split(",");
        String date = splittedDateTime[0];
        String time = splittedDateTime[1];
        String[] splittedTime = time.trim().split(":");
        int hours = Integer.valueOf(splittedTime[0]);
        int minutes = Integer.valueOf(splittedTime[1]);
        switch (date) {
            case TODAY:
                return new LocalDateTime().withTime(hours, minutes, 0, 0);
            case YESTERDAY:
                return new LocalDateTime().minusDays(1).withTime(hours, minutes, 0, 0);
            default:
                String month = date.split(SPACE_SPLITTER)[1];
                return LocalDateTime.parse(dtString.replace(month, MONTHS.get(month)), commonFormatter);
        }
    }

    public static LocalDateTime getParsingDateLimit(Config config, Store<Vacancy> store) {
        LocalDateTime now = LocalDateTime.now();
        return getLastParsingDate.apply(store)
                .map(ldt -> now.minusDays(config.getInt("parser.parse_period_days")))
                .orElseGet(() -> now.minusYears(config.getInt("parser.parse_period_years")));
    }

    private static Function<Store<Vacancy>, Optional<LocalDateTime>> getLastParsingDate = store -> store.findRecent(1).stream().findFirst().map(Vacancy::getCreated);
}
