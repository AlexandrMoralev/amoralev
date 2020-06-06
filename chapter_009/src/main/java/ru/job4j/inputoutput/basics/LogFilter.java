package ru.job4j.inputoutput.basics;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

public class LogFilter {

    public static final String ERROR_CODE = "404";
    public static final int INDEX_FROM_TAIL = 1;

    public static void main(String[] args) {
        List<String> log = filter("log.txt");
        System.out.println(log);
        save(log, "404.txt");
    }

    public static List<String> filter(String file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return reader.lines()
                    .filter(containsRequiredElement)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public static void save(List<String> log, String file) {
        try (PrintWriter writer = new PrintWriter(
                new BufferedOutputStream(
                        new FileOutputStream(file.trim())
                ))) {
            log.forEach(writer::println);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static Predicate<String> containsRequiredElement = line -> {
        List<String> ln = Arrays.stream(line.strip().split(" "))
                .map(String::strip)
                .filter(not(String::isBlank))
                .collect(Collectors.toList());
        if (ln.isEmpty() || ln.size() < INDEX_FROM_TAIL + 1) {
            return false;
        }
        String code = ln.get(ln.size() - 1 - INDEX_FROM_TAIL);
        return ERROR_CODE.equals(code);
    };

}
