package ru.job4j.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;
import static ru.job4j.common.Constants.*;

public class NamedArgs {

    private static final int ARGS_SIZE = 7;

    private final Map<String, String> parsedArgs;
    private String searchCriteria;

    private NamedArgs() {
        parsedArgs = new HashMap<>();
    }

    public static NamedArgs of(final String[] args) {
        NamedArgs names = new NamedArgs();
        names.parse(args);
        return names;
    }

    public String get(String key) {
        return this.parsedArgs.get(key);
    }

    public String getSearchCriteria() {
        return this.searchCriteria;
    }

    private void parse(String[] args) {
        validateArgs(args);

        for (int i = 0; i < args.length; i++) {
            if (SEARCH_METHODS.contains(args[i].strip())) {
                searchCriteria = args[i].strip();
                continue;
            }
            this.parsedArgs.put(args[i].strip(), args[++i].strip());
        }
    }

    private void validateArgs(String[] args) {
        if (args.length != ARGS_SIZE) {
            throw new IllegalArgumentException(TOOLTIP);
        }
        Set<String> keys = Arrays.stream(args)
                .map(String::strip)
                .filter(not(String::isBlank))
                .filter(s -> s.startsWith("-"))
                .collect(Collectors.toSet());
        String absentArgs = MAIN_ARGS.stream().filter(not(keys::contains)).collect(Collectors.joining(", "));
        if (!absentArgs.isBlank()) {
            throw new IllegalArgumentException(String.format("Not enough args. %s not found", absentArgs));
        }
        if (SEARCH_METHODS.stream().noneMatch(keys::contains)) {
            throw new IllegalArgumentException("No search method found");
        }
    }

}
