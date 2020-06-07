package ru.job4j.inputoutput.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Predicate;

public class Config {
    private final String path;
    private final Map<String, String> values;

    public Config(final String path) {
        this.path = path.strip();
        this.values = new HashMap<>();
    }

    public void load() {
        try (BufferedReader reader = new BufferedReader(new FileReader(this.path))) {
            reader.lines()
                    .map(String::strip)
                    .filter(validLines)
                    .map(v -> v.split("="))
                    .filter(validKVPairs)
                    .forEach(entry -> this.values.put(entry[0].strip(), entry[1].strip()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Predicate<String> validLines = v -> !v.isBlank() && !v.startsWith("#") && v.contains("=");
    private Predicate<String[]> validKVPairs = arr -> arr.length == 2 && !arr[0].strip().isBlank() && !arr[1].strip().isBlank();

    public String value(String key) {
        return values.get(key);
    }

    @Override
    public String toString() {
        StringJoiner out = new StringJoiner(System.lineSeparator());
        this.values.entrySet().stream().map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue())).forEach(out::add);
        return out.toString();
    }

    public static void main(String[] args) {
        Config config = new Config("app.properties");
        config.load();
        System.out.println(config);
    }

}
