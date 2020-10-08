package ru.job4j.entity.enumerations;

import java.util.stream.Stream;

public enum Make {

    LAND_ROVER("landRover"), // TODO add more brands
    RENAULT("renault");

    private final String brand;

    Make(String brand) {
        this.brand = brand;
    }

    @Override
    public String toString() {
        return this.brand;
    }

    public static Make of(String brand) {
        return Stream.of(Make.values())
                .filter(v -> brand.equalsIgnoreCase(v.brand))
                .findFirst()
                .orElseThrow(RuntimeException::new); // TODO replace with specific exception
    }
}
