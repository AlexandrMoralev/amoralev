package ru.job4j;

import ru.job4j.common.NamedArgs;
import ru.job4j.impl.search.FileSearch;
import ru.job4j.util.ResultWriter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Collectors;

public class FileFinder {

    public static void main(String[] args) throws IOException {
        NamedArgs params = NamedArgs.of(args);

        Collection<String> resultFilePaths = new FileSearch().findFiles(params).stream()
                .map(Path::toAbsolutePath)
                .map(Path::toString)
                .collect(Collectors.toList());

        ResultWriter.writeToFile(resultFilePaths, params);
    }
}
