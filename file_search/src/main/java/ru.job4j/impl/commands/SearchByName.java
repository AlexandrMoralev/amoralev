package ru.job4j.impl.commands;

import ru.job4j.SearchCommand;
import ru.job4j.common.NamedArgs;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static java.nio.file.FileVisitResult.CONTINUE;
import static ru.job4j.common.Constants.SEARCH_CRITERIA;

public class SearchByName<T extends Path> extends SearchCommand<T> {

    private final String searchFileName;

    public SearchByName(NamedArgs params) {
        super(params);
        searchFileName = params.get(SEARCH_CRITERIA);
    }

    @Override
    public Collection<T> getPaths() {
        return new ArrayList<>(paths);
    }

    @Override
    public FileVisitResult visitFile(T file, BasicFileAttributes attrs) throws IOException {
        Optional.of(file)
                .filter(path -> searchFileName.toLowerCase().contains(path.toFile().getName().toLowerCase()))
                .ifPresent(paths::add);
        return CONTINUE;
    }

}