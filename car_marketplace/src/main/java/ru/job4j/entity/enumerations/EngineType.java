package ru.job4j.entity.enumerations;

import java.util.Collection;
import java.util.EnumSet;

public enum EngineType {

    GASOLINE_92("gasoline_92"),
    GASOLINE_95("gasoline_95"),
    GASOLINE_98("gasoline_98"),
    GASOLINE_100("gasoline_100"),
    DIESEL("diesel"),
    HYBRID("hybrid"),
    COMPRESSED_NATURAL_GAS("gas"),
    ELECTRIC("electric");


    private static Collection<EngineType> values = EnumSet.allOf(EngineType.class);

    public static Collection<EngineType> getValues() {
        return values;
    }

    private final String code;

    EngineType(String description) {
        this.code = description;
    }

    @Override
    public String toString() {
        return this.code;
    }

}
