package ru.job4j.entity.enumerations;

import java.util.Collection;
import java.util.EnumSet;

public enum ItemType {

    CAR,
    COMMERCIAL,
    MOTORCYCLE,
    PARTS;

    private static Collection<ItemType> values = EnumSet.allOf(ItemType.class);

    public static Collection<ItemType> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
