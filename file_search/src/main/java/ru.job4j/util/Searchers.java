package ru.job4j.util;

import ru.job4j.SearchCommand;
import ru.job4j.common.NamedArgs;
import ru.job4j.impl.commands.SearchByDefault;
import ru.job4j.impl.commands.SearchByName;
import ru.job4j.impl.commands.SearchByRegexp;
import ru.job4j.impl.commands.SearchByWildcard;

import java.util.Map;
import java.util.function.Function;

import static ru.job4j.common.Constants.*;

public class Searchers {

    private static final Map<String, Function<NamedArgs, SearchCommand>> SEARCH_METHODS = Map.of(
            SEARCH_BY_MASK, SearchByWildcard::new,
            SEARCH_BY_FILENAME, SearchByName::new,
            SEARCH_BY_REGEXP, SearchByRegexp::new,
            SEARCH_BY_DEFAULT, SearchByDefault::new
    );

    public static SearchCommand getSearcher(NamedArgs params) {
        String searchCriteria = params.getSearchCriteria();
        return SEARCH_METHODS.getOrDefault(searchCriteria, SEARCH_METHODS.get(SEARCH_BY_DEFAULT)).apply(params);
    }

}
