package ru.job4j.entity.enumerations;

import java.util.stream.Stream;

public enum BodyStyle {

    SEDAN("sedan"); // TODO add more styles

    private final String code;

    BodyStyle(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return this.code;
    }

    public static BodyStyle of(String code) {
        return Stream.of(BodyStyle.values())
                .filter(v -> code.equalsIgnoreCase(v.code))
                .findFirst()
                .orElseThrow(RuntimeException::new); // TODO replace with specific exception
    }
}
