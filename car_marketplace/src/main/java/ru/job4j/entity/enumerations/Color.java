package ru.job4j.entity.enumerations;

import java.util.Collection;
import java.util.EnumSet;

public enum Color {

    BEIGE,
    BLACK,
    BLUE,
    BROWN,
    GOLD,
    GRAY,
    GREEN,
    ORANGE,
    PINK,
    PURPLE,
    RED,
    SILVER,
    WHITE,
    YELLOW,
    OTHER;

    private static Collection<Color> values = EnumSet.allOf(Color.class);

    public static Collection<Color> getValues() {
        return values;
    }

}
