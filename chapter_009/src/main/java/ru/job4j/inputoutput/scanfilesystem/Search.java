package ru.job4j.inputoutput.scanfilesystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

public class Search {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            throw new IllegalArgumentException("No args. Usage java -jar dir.jar startFolder ext1,ext2,ext3");
        }
        if (args.length != 2) {
            throw new IllegalArgumentException("No args. Usage java -jar dir.jar startFolder ext1,ext2,ext3");
        }
        String startFolder = args[0].strip();
        List<String> extensions = Arrays.stream(args[1].strip().split(","))
                .map(String::strip)
                .filter(not(String::isBlank))
                .collect(Collectors.toList());
        Path start = Paths.get(startFolder);
//        Files.walkFileTree(start, new PrintFiles());
        search(start, extensions).forEach(System.out::println);
    }

    public static List<Path> search(Path root, List<String> ext) throws IOException {
        SearchFiles<Path> searcher = new SearchFiles<>(p -> {
            String fileName = p.toFile().getName();
            return ext.stream().anyMatch(fileName::endsWith);
        });
        Files.walkFileTree(root, searcher);
        return searcher.getPaths();
    }

    public static List<Path> search(Path root, Predicate<Path> filter) throws IOException {
        SearchFiles<Path> searcher = new SearchFiles<>(filter);
        Files.walkFileTree(root, searcher);
        return searcher.getPaths();
    }
}
