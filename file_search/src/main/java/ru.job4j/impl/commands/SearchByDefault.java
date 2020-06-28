package ru.job4j.impl.commands;

import ru.job4j.SearchCommand;
import ru.job4j.common.NamedArgs;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;

public class SearchByDefault<T extends Path> extends SearchCommand<T> {

    public SearchByDefault(NamedArgs params) {
        super(params);
    }

    @Override
    public Collection<T> getPaths() {
        return Collections.emptyList();
    }

}
