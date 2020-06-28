package ru.job4j.common;

import java.util.Set;

public final class Constants {

    public static final String START_DIRECTORY = "-d";
    public static final String SEARCH_CRITERIA = "-n";
    public static final String OUTPUT_FILEPATH = "-o";

    public static final String SEARCH_BY_MASK = "-m";
    public static final String SEARCH_BY_FILENAME = "-f";
    public static final String SEARCH_BY_REGEXP = "-r";
    public static final String SEARCH_BY_DEFAULT = "-default";

    public static final Set<String> MAIN_ARGS = Set.of(
            START_DIRECTORY,
            SEARCH_CRITERIA,
            OUTPUT_FILEPATH
    );

    public static final Set<String> SEARCH_METHODS = Set.of(
            SEARCH_BY_MASK,
            SEARCH_BY_FILENAME,
            SEARCH_BY_REGEXP,
            SEARCH_BY_DEFAULT
    );

    public static final String TOOLTIP = createTooltip();

    private static String createTooltip() {
        StringBuilder sb = new StringBuilder();
        String separator = System.lineSeparator();
        sb.append(String.format("Запуск: -jar find.jar %s c:/ %s *.txt %s %s log.txt", START_DIRECTORY, SEARCH_CRITERIA, SEARCH_BY_MASK, OUTPUT_FILEPATH)).append(separator);
        sb.append("Ключи: ").append(separator);
        sb.append(String.format("%s - директория в которой начинать поиск.", START_DIRECTORY)).append(separator);
        sb.append(String.format("%s - критерий поиска: имя файла, маска, либо регулярное выражение.", SEARCH_CRITERIA)).append(separator);
        sb.append(String.format("%s - искать по маске, либо %s - полное совпадение имени, либо %s регулярное выражение.", SEARCH_BY_MASK, SEARCH_BY_FILENAME, SEARCH_BY_REGEXP)).append(separator);
        sb.append(String.format("%s - результат записать в файл.", OUTPUT_FILEPATH)).append(separator);
        return sb.toString();
    }
}
