package ru.job4j.entity.enumerations;

import java.util.stream.Stream;

public enum Color {

    RED("red"); // TODO add colors

    private final String code;

    Color(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return this.code;
    }

    public static Color of(String code) {
        return Stream.of(Color.values())
                .filter(v -> code.equalsIgnoreCase(v.code))
                .findFirst()
                .orElseThrow(RuntimeException::new); // TODO replace with specific exception
    }
}
