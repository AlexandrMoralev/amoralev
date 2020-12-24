package ru.job4j.entity.enumerations;

import java.util.Collection;
import java.util.EnumSet;

public enum Make {

    ACURA,
    ALFA_ROMEO,
    ASTON_MARTIN,
    AUDI,
    BENTLEY,
    BMW,
    BUICK,
    CADILLAC,
    CHEVROLET,
    CHRYSLER,
    CITROEN,
    DAIMLER,
    DODGE,
    EXCALIBUR,
    FERRARI,
    FIAT,
    FORD,
    GMC,
    HONDA,
    HUMMER,
    HYUNDAI,
    INFINITI,
    ISUZU,
    JAGUAR,
    JEEP,
    KIA,
    LAMBORGHINI,
    LAND_ROVER,
    LEXUS,
    LINCOLN,
    LUXGEN,
    MASERATI,
    MAYBACH,
    MAZDA,
    MCLAREN,
    MERCEDES,
    MITSUBISHI,
    NISSAN,
    OLDSMOBILE,
    OPEL,
    PEUGEOT,
    PLYMOUTH,
    PONTIAC,
    PORSCHE,
    RAM,
    RENAULT,
    ROLLS_ROYCE,
    ROVER,
    SAAB,
    SEAT,
    SKODA,
    SMART,
    SUBARU,
    SUZUKI,
    TESLA,
    TOYOTA,
    VOLKSWAGEN,
    VOLVO;

    private static Collection<Make> values = EnumSet.allOf(Make.class);

    public static Collection<Make> getValues() {
        return values;
    }

}
