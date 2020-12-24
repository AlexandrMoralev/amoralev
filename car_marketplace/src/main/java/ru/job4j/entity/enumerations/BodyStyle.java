package ru.job4j.entity.enumerations;

import java.util.Collection;
import java.util.EnumSet;

public enum BodyStyle {

    SUV,
    SEDAN,
    PICKUP,
    COUPE,
    CONVERTIBLE,
    HATCHBACK,
    WAGON,
    MINIVAN,
    CARGO;

    private static Collection<BodyStyle> values = EnumSet.allOf(BodyStyle.class);

    public static Collection<BodyStyle> getValues() {
        return values;
    }

}
