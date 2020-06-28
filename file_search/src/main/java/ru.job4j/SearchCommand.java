package ru.job4j;

import ru.job4j.common.NamedArgs;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.nio.file.FileVisitResult.CONTINUE;

public abstract class SearchCommand<T extends Path> implements FileVisitor<T> {

    protected final NamedArgs params;
    protected final List<T> paths;

    protected SearchCommand(NamedArgs params) {
        this.params = params;
        this.paths = new ArrayList<>();
    }

    public abstract Collection<T> getPaths();

    @Override
    public FileVisitResult preVisitDirectory(T dir, BasicFileAttributes attrs) throws IOException {
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(T file, BasicFileAttributes attrs) throws IOException {
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(T file, IOException exc) throws IOException {
        return CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(T dir, IOException exc) throws IOException {
        return CONTINUE;
    }

}
