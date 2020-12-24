package ru.job4j.entity.enumerations;

import java.util.Collection;
import java.util.EnumSet;

public enum TransmissionType {

    MANUAL("manual"),
    AUTOMATIC("automatic"),
    AUTOMANUAL("automanual"),
    CVT("cvt");


    private static Collection<TransmissionType> values = EnumSet.allOf(TransmissionType.class);

    public static Collection<TransmissionType> getValues() {
        return values;
    }

    private final String description;

    TransmissionType(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
