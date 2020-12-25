package ru.job4j.persistence;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.TestAppContext;
import ru.job4j.entity.Engine;
import ru.job4j.entity.ProductionInfo;
import ru.job4j.entity.Transmission;
import ru.job4j.entity.enumerations.Color;
import ru.job4j.entity.enumerations.DriveType;
import ru.job4j.entity.enumerations.Make;
import ru.job4j.persistence.impl.EngineDao;
import ru.job4j.persistence.impl.ProductionInfoDao;
import ru.job4j.persistence.impl.TransmissionDao;

import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static ru.job4j.TestUtils.*;

public class ProductionInfoDaoTest {

    private final TestAppContext context = TestAppContext.INSTANCE;
    private final ProductionInfoDao productionInfoDao = context.productionInfoDao;
    private final EngineDao engineDao = context.engineDao;
    private final TransmissionDao transmissionDao = context.transmissionDao;

    @AfterEach
    public void cleanUp() {
        this.productionInfoDao.findAll().forEach(productionInfoDao::delete);
        this.engineDao.findAll().forEach(engineDao::delete);
        this.transmissionDao.findAll().forEach(transmissionDao::delete);
    }

    @Test
    public void whenAddProductionInfoThenStorageShouldContainTheSameEntity() {

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

        ProductionInfo result = this.productionInfoDao.find(productionInfo.getId()).orElseThrow(() -> new RuntimeException("productionInfo entity not found"));

        assertEquals(productionInfo, result);
        assertEquals(1, this.productionInfoDao.findAll().size());
    }

    @Test
    public void createDuplicateProductionInfoCauseErrors() {
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
        ProductionInfo duplicateProductionInfo = ProductionInfo.create(
                Make.AUDI,
                producedAt,
                dieselEngine,
                manualTransmission,
                DriveType.ALL_WHEEL_DRIVE,
                Color.GREEN
        );
        this.productionInfoDao.save(productionInfo);

        Exception exception = assertThrows(ConstraintViolationException.class,
                () -> this.productionInfoDao.save(duplicateProductionInfo)
        );
        assertEquals("could not execute statement", exception.getMessage());
    }

    @Test
    public void whenUpdateProductionInfoThenCauseErrors() {
        LocalDateTime producedAt = LocalDateTime.now().minusMonths(10);

        Engine dieselEngine = getDieselEngine();
        Engine electricEngine = getElectricEngine();
        this.engineDao.save(dieselEngine);
        this.engineDao.save(electricEngine);

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

        Optional<ProductionInfo> saved = this.productionInfoDao.find(productionInfo.getId());

        Exception exception = assertThrows(PersistenceException.class,
                () ->
                        saved.map(info -> {
                            info.setEngine(electricEngine);
                            info.setTransmission(manualTransmission);
                            info.setColor(Color.RED);
                            info.setMake(Make.ACURA);
                            info.setDriveType(DriveType.FRONT_WHEEL_DRIVE);
                            return info;
                        }).ifPresent(this.productionInfoDao::update)
        );
        assertTrue(exception.getMessage().contains("An immutable natural identifier of entity"));
        assertTrue(exception.getMessage().contains("was altered"));
    }

    @Test
    public void whenDeleteProductionInfoThenStorageShouldNotContainTheEntity() {
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

        Optional<ProductionInfo> saved1 = this.productionInfoDao.find(productionInfo1.getId());
        Optional<ProductionInfo> saved2 = this.productionInfoDao.find(productionInfo2.getId());

        assertTrue(saved1.isPresent());
        assertTrue(saved2.isPresent());

        assertEquals(2, this.productionInfoDao.findAll().size());

        this.productionInfoDao.delete(productionInfo1);
        this.productionInfoDao.deleteById(productionInfo2.getId());

        assertTrue(this.productionInfoDao.find(productionInfo1.getId()).isEmpty());
        assertTrue(this.productionInfoDao.find(productionInfo2.getId()).isEmpty());
        assertTrue(this.productionInfoDao.findAll().isEmpty());
    }
}
