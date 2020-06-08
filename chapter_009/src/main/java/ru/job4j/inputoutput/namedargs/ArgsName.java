package ru.job4j.inputoutput.namedargs;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public class ArgsName {

    private final Map<String, String> values = new HashMap<>();

    public String get(String key) {
        return values.get(key);
    }

    private void parse(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("No args. Usage java -jar dir.jar -key1=value1 -key2=value2 etc");
        }
        values.putAll(
                Arrays.stream(args)
                        .map(String::strip)
                        .filter(not(String::isBlank))
                        .map(v -> v.split(" -"))
                        .flatMap(Stream::of)
                        .map(String::strip)
                        .filter(not(String::isBlank))
                        .map(s -> s.split("="))
                        .collect(
                                Collectors.toMap(
                                        arr -> arr[0].replaceFirst("-", "").strip(),
                                        arr -> arr[1].strip(),
                                        (existing, replacement) -> existing)
                        )
        );
    }

    public static ArgsName of(String[] args) {
        ArgsName names = new ArgsName();
        names.parse(args);
        return names;
    }

    public static void main(String[] args) {
        ArgsName jvm = ArgsName.of(new String[]{"-Xmx=512", "-encoding=UTF-8"});
        System.out.println(jvm.get("Xmx"));

        ArgsName zip = ArgsName.of(new String[]{"-out=project.zip", "-encoding=UTF-8"});
        System.out.println(zip.get("out"));
    }
}
