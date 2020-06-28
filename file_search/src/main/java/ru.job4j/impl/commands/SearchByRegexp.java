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
import java.util.regex.Pattern;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static ru.job4j.common.Constants.SEARCH_CRITERIA;

public class SearchByRegexp<T extends Path> extends SearchCommand<T> {

    private final Pattern pattern;

    public SearchByRegexp(NamedArgs params) {
        super(params);
        pattern = Pattern.compile(params.get(SEARCH_CRITERIA), CASE_INSENSITIVE);
    }

    @Override
    public Collection<T> getPaths() {
        return new ArrayList<>(super.paths);
    }

    @Override
    public FileVisitResult visitFile(T file, BasicFileAttributes attrs) throws IOException {
        Optional.of(file)
                .filter(path -> pattern.matcher(path.toFile().getName()).matches())
                .ifPresent(paths::add);
        return CONTINUE;
    }

}