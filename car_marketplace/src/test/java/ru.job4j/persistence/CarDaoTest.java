package ru.job4j.persistence;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.TestAppContext;
import ru.job4j.entity.Car;
import ru.job4j.entity.Engine;
import ru.job4j.entity.ProductionInfo;
import ru.job4j.entity.Transmission;
import ru.job4j.entity.enumerations.BodyStyle;
import ru.job4j.entity.enumerations.Color;
import ru.job4j.entity.enumerations.DriveType;
import ru.job4j.entity.enumerations.Make;
import ru.job4j.persistence.impl.CarDao;
import ru.job4j.persistence.impl.EngineDao;
import ru.job4j.persistence.impl.ProductionInfoDao;
import ru.job4j.persistence.impl.TransmissionDao;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static ru.job4j.TestUtils.*;

public class CarDaoTest {

    private final TestAppContext context = TestAppContext.INSTANCE;
    private final CarDao carDao = context.carDao;
    private final ProductionInfoDao productionInfoDao = context.productionInfoDao;
    private final EngineDao engineDao = context.engineDao;
    private final TransmissionDao transmissionDao = context.transmissionDao;

    @AfterEach
    public void cleanUp() {
        this.carDao.findAll().forEach(carDao::delete);
        this.productionInfoDao.findAll().forEach(productionInfoDao::delete);
        this.engineDao.findAll().forEach(engineDao::delete);
        this.transmissionDao.findAll().forEach(transmissionDao::delete);
    }

    @Test
    public void whenAddCarThenStorageShouldContainTheSameEntity() {

        Engine dieselEngine = getDieselEngine();
        this.engineDao.save(dieselEngine);

        Transmission manualTransmission = getManualTransmission();
        this.transmissionDao.save(manualTransmission);

        ProductionInfo productionInfo = ProductionInfo.create(
                Make.AUDI,
                LocalDateTime.now().minusMonths(10),
                dieselEngine,
                manualTransmission,
                DriveType.ALL_WHEEL_DRIVE,
                Color.GREEN
        );
        this.productionInfoDao.save(productionInfo);

        Car car = getCoupeCar(productionInfo);
        this.carDao.save(car);

        Car result = this.carDao.find(car.getId()).orElseThrow(() -> new RuntimeException("car entity not found"));

        assertEquals(car, result);
        assertEquals(1, this.carDao.findAll().count());
    }

    @Test
    public void createDuplicateCarCauseErrors() {
        LocalDateTime producedAt = LocalDateTime.now().minusMonths(10);

        Engine dieselEngine = getDieselEngine();
        this.engineDao.save(dieselEngine);

        Transmission manualTransmission = getManualTransmission();
        this.transmissionDao.save(manualTransmission);

        ProductionInfo productionInfo = ProductionInfo.create(
                Make.AUDI,
                producedAt,
                dieselEngine,
                manualTransmission,
                DriveType.ALL_WHEEL_DRIVE,
                Color.GREEN
        );
        this.productionInfoDao.save(productionInfo);

        Car car = getCoupeCar(productionInfo);
        this.carDao.save(car);

        Car duplicateCar = getCoupeCar(productionInfo);

        Exception exception = assertThrows(ConstraintViolationException.class,
                () -> this.carDao.save(duplicateCar)
        );
        assertEquals("could not execute statement", exception.getMessage());
    }

    @Test
    public void createDuplicateCarByDifferUsersCauseErrors() {
        LocalDateTime producedAt = LocalDateTime.now().minusMonths(10);

        Engine dieselEngine = getDieselEngine();
        this.engineDao.save(dieselEngine);

        Transmission manualTransmission = getManualTransmission();
        this.transmissionDao.save(manualTransmission);

        ProductionInfo productionInfo = ProductionInfo.create(
                Make.AUDI,
                producedAt,
                dieselEngine,
                manualTransmission,
                DriveType.ALL_WHEEL_DRIVE,
                Color.GREEN
        );

        this.productionInfoDao.save(productionInfo);

        Car car = getCoupeCar(productionInfo);
        this.carDao.save(car);

        Car duplicateCar = getCoupeCar(productionInfo);

        Exception exception = assertThrows(ConstraintViolationException.class,
                () -> this.carDao.save(duplicateCar)
        );
        assertEquals("could not execute statement", exception.getMessage());
    }


    @Test
    public void whenUpdateCarThenStorageShouldContainTheUpdatedEntity() {

        Engine dieselEngine = getDieselEngine();
        Engine electricEngine = getElectricEngine();
        this.engineDao.save(dieselEngine);
        this.engineDao.save(electricEngine);

        Transmission cvtTransmission = getCvtTransmission();
        Transmission manualTransmission = getManualTransmission();
        this.transmissionDao.save(cvtTransmission);
        this.transmissionDao.save(manualTransmission);

        ProductionInfo productionInfo1 = ProductionInfo.create(
                Make.AUDI,
                LocalDateTime.now().minusMonths(10),
                dieselEngine,
                cvtTransmission,
                DriveType.ALL_WHEEL_DRIVE,
                Color.GREEN
        );
        ProductionInfo productionInfo2 = ProductionInfo.create(
                Make.BMW,
                LocalDateTime.now().minusMonths(8),
                electricEngine,
                manualTransmission,
                DriveType.REAR_WHEEL_DRIVE,
                Color.WHITE
        );
        this.productionInfoDao.save(productionInfo1);
        this.productionInfoDao.save(productionInfo2);

        Car car1 = getCoupeCar(productionInfo1);
        Car car2 = getCoupeCar(productionInfo2);
        this.carDao.save(car1);
        this.carDao.save(car2);

        Optional<Car> saved = this.carDao.find(car1.getId());

        assertTrue(saved.isPresent());

        saved.map(car -> {
            car.setBodyStyle(BodyStyle.HATCHBACK);
            car.setMileage(20000);
            car.setPrice(70000L);
            return car;
        }).ifPresent(carDao::update);

        Car updated = this.carDao.find(saved.get().getId()).orElseThrow(() -> new RuntimeException("car entity not found"));

        assertEquals(BodyStyle.HATCHBACK, updated.getBodyStyle());
        assertEquals(20000, updated.getMileage());
        assertEquals(70000L, updated.getPrice());
        assertEquals(productionInfo1, updated.getProductionInfo());

        Car notUpdated = this.carDao.find(car2.getId()).orElseThrow(() -> new RuntimeException("car entity not found"));
        assertEquals(BodyStyle.COUPE, notUpdated.getBodyStyle());
        assertEquals(10000, notUpdated.getMileage());
        assertEquals(50000L, notUpdated.getPrice());
        assertEquals(productionInfo2, notUpdated.getProductionInfo());

        assertEquals(2, this.carDao.findAll().count());
    }

    @Test
    public void whenUpdateCarWithProductInfoThenCarShouldNotBeUpdated() {

        Engine dieselEngine = getDieselEngine();
        Engine electricEngine = getElectricEngine();
        this.engineDao.save(dieselEngine);
        this.engineDao.save(electricEngine);

        Transmission cvtTransmission = getCvtTransmission();
        Transmission manualTransmission = getManualTransmission();
        this.transmissionDao.save(cvtTransmission);
        this.transmissionDao.save(manualTransmission);

        ProductionInfo productionInfo1 = ProductionInfo.create(
                Make.AUDI,
                LocalDateTime.now().minusMonths(10),
                dieselEngine,
                cvtTransmission,
                DriveType.ALL_WHEEL_DRIVE,
                Color.GREEN
        );
        ProductionInfo productionInfo2 = ProductionInfo.create(
                Make.BMW,
                LocalDateTime.now().minusMonths(8),
                electricEngine,
                manualTransmission,
                DriveType.REAR_WHEEL_DRIVE,
                Color.WHITE
        );
        this.productionInfoDao.save(productionInfo1);
        this.productionInfoDao.save(productionInfo2);

        Car car = getCoupeCar(productionInfo1);
        this.carDao.save(car);

        Optional<Car> saved = this.carDao.find(car.getId());
        assertTrue(saved.isPresent());
        assertEquals(1, this.carDao.findAll().count());

        saved.map(c -> {
            c.setProductionInfo(productionInfo2);
            return c;
        }).ifPresent(carDao::update);

        Car updated = this.carDao.find(saved.get().getId()).orElseThrow(() -> new RuntimeException("car entity not found"));
        assertEquals(BodyStyle.COUPE, updated.getBodyStyle());
        assertEquals(10000, updated.getMileage());
        assertEquals(50000L, updated.getPrice());
        assertEquals(productionInfo1, updated.getProductionInfo());

        assertEquals(1, this.carDao.findAll().count());

    }

    @Test
    public void whenDeleteCarThenStorageShouldNotContainTheEntity() {
        Engine dieselEngine = getDieselEngine();
        Engine electricEngine = getElectricEngine();
        this.engineDao.save(dieselEngine);
        this.engineDao.save(electricEngine);

        Transmission cvtTransmission = getCvtTransmission();
        Transmission manualTransmission = getManualTransmission();
        this.transmissionDao.save(cvtTransmission);
        this.transmissionDao.save(manualTransmission);

        ProductionInfo productionInfo1 = ProductionInfo.create(
                Make.AUDI,
                LocalDateTime.now().minusMonths(10),
                dieselEngine,
                cvtTransmission,
                DriveType.ALL_WHEEL_DRIVE,
                Color.GREEN
        );
        ProductionInfo productionInfo2 = ProductionInfo.create(
                Make.BMW,
                LocalDateTime.now().minusMonths(8),
                electricEngine,
                manualTransmission,
                DriveType.REAR_WHEEL_DRIVE,
                Color.WHITE
        );

        this.productionInfoDao.save(productionInfo1);
        this.productionInfoDao.save(productionInfo2);

        Car car1 = getCoupeCar(productionInfo1);
        Car car2 = getCoupeCar(productionInfo2);
        this.carDao.save(car1);
        this.carDao.save(car2);

        Optional<Car> saved1 = this.carDao.find(car1.getId());
        Optional<Car> saved2 = this.carDao.find(car2.getId());

        assertTrue(saved1.isPresent());
        assertTrue(saved2.isPresent());

        assertEquals(2, this.carDao.findAll().count());

        this.carDao.delete(car1);
        this.carDao.deleteById(car2.getId());

        assertTrue(this.carDao.find(car1.getId()).isEmpty());
        assertTrue(this.carDao.find(car2.getId()).isEmpty());
        assertEquals(0, this.carDao.findAll().count());

        assertEquals(2, this.productionInfoDao.findAll().count());
        assertEquals(2, this.engineDao.findAll().count());
        assertEquals(2, this.transmissionDao.findAll().count());
    }

}

