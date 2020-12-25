package ru.job4j.entity.enumerations;

import java.util.Collection;
import java.util.EnumSet;

public enum DriveType {

    ALL_WHEEL_DRIVE,
    FRONT_WHEEL_DRIVE,
    REAR_WHEEL_DRIVE;

    private static Collection<DriveType> values = EnumSet.allOf(DriveType.class);

    public static Collection<DriveType> getValues() {
        return values;
    }
}
