package ru.job4j.inputoutput.scanfilesystem;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static java.nio.file.FileVisitResult.CONTINUE;

public class SearchFiles<T extends Path> implements FileVisitor<T> {

    private final Predicate<T> requiredFiles;
    private final List<T> paths;

    public SearchFiles(final Predicate<T> filter) {
        this.requiredFiles = filter;
        this.paths = new ArrayList<>();
    }

    @Override
    public FileVisitResult preVisitDirectory(T dir, BasicFileAttributes attrs) throws IOException {
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(T file, BasicFileAttributes attrs) throws IOException {
        Optional.of(file).filter(requiredFiles).ifPresent(paths::add);
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

    public List<T> getPaths() {
        return this.paths;
    }
}
