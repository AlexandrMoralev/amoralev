package ru.job4j.entity.enumerations;

import java.util.stream.Stream;

public enum TransmissionType {

    MANUAL("manual"), // TODO add all
    AUTO("auto"),
    ROBOT("robot");

    private final String description;

    TransmissionType(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.description;
    }

    public static TransmissionType of(String description) {
        return Stream.of(TransmissionType.values())
                .filter(v -> description.equalsIgnoreCase(v.description))
                .findFirst()
                .orElseThrow(RuntimeException::new); // TODO replace with specific exception
    }
}
