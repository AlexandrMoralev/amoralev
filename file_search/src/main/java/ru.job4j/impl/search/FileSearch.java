package ru.job4j.impl.search;

import ru.job4j.Search;
import ru.job4j.SearchCommand;
import ru.job4j.common.NamedArgs;
import ru.job4j.util.Searchers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

import static ru.job4j.common.Constants.START_DIRECTORY;

public class FileSearch implements Search {

    @Override
    public Collection<Path> findFiles(final NamedArgs params) throws IOException {
        Path root = Path.of(params.get(START_DIRECTORY));
        SearchCommand<Path> searcher = Searchers.getSearcher(params);
        Files.walkFileTree(root, searcher);
        return searcher.getPaths();
    }

}
