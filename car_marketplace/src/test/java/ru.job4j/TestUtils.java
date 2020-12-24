package ru.job4j;

import ru.job4j.entity.*;
import ru.job4j.entity.enumerations.BodyStyle;
import ru.job4j.entity.enumerations.EngineType;
import ru.job4j.entity.enumerations.TransmissionType;

import java.time.LocalDateTime;


public enum TestUtils {

    INSTANCE;

    public static Engine getDieselEngine() {
        return Engine.create("diesel engine model", 120, 2000, EngineType.DIESEL);
    }

    public static Engine getElectricEngine() {
        return Engine.create("electric engine model", 1200, 0, EngineType.ELECTRIC);
    }

    public static Engine getGasEngine() {
        return Engine.create("gas engine model", 1200, 1000, EngineType.COMPRESSED_NATURAL_GAS);
    }

    public static Transmission getManualTransmission() {
        return Transmission.create("transmission model_2", TransmissionType.MANUAL);
    }

    public static Transmission getCvtTransmission() {
        return Transmission.create("transmission model_1", TransmissionType.CVT);
    }

    public static Transmission getAutoTransmission() {
        return Transmission.create("transmission_model", TransmissionType.AUTOMATIC);
    }

    public static User getUserWithPhone(String phone) {
        return User.create(
                phone,
                "",
                "",
                "",
                LocalDateTime.now()
        );
    }

    public static Car getCoupeCar(ProductionInfo productionInfo) {
        return Car.create(
                productionInfo,
                BodyStyle.COUPE,
                10000,
                50000L
        );
    }

    public static Car getHatchbackCar(ProductionInfo productionInfo) {
        return Car.create(
                productionInfo,
                BodyStyle.HATCHBACK,
                20000,
                150000L
        );
    }

}
