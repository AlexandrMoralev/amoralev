package ru.job4j.persistence;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.TestAppContext;
import ru.job4j.entity.*;
import ru.job4j.entity.enumerations.Color;
import ru.job4j.entity.enumerations.DriveType;
import ru.job4j.entity.enumerations.ItemType;
import ru.job4j.entity.enumerations.Make;
import ru.job4j.persistence.impl.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static ru.job4j.TestUtils.*;

public class ItemsDaoTest {

    private final TestAppContext context = TestAppContext.INSTANCE;
    private final ItemsDao itemsDao = context.itemsDao;
    private final UsersDao usersDao = context.usersDao;
    private final CarDao carDao = context.carDao;
    private final ProductionInfoDao productionInfoDao = context.productionInfoDao;
    private final EngineDao engineDao = context.engineDao;
    private final TransmissionDao transmissionDao = context.transmissionDao;
    private final PhotoDao photoDao = context.photoDao;

    @AfterEach
    public void cleanUp() {
        this.usersDao.findAll().forEach(usersDao::delete);
        this.itemsDao.findAll().forEach(itemsDao::delete);
        this.carDao.findAll().forEach(carDao::delete);
        this.productionInfoDao.findAll().forEach(productionInfoDao::delete);
        this.engineDao.findAll().forEach(engineDao::delete);
        this.transmissionDao.findAll().forEach(transmissionDao::delete);
        this.photoDao.findAll().forEach(photoDao::delete);
    }

    @Test
    public void whenAddItemThenStorageShouldContainTheSameEntity() {

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

        User owner = getUserWithPhone("1");
        this.usersDao.save(owner);

        Car car = getCoupeCar(productionInfo);
        this.carDao.save(car);

        Item item = Item.create(
                ItemType.CAR,
                owner,
                car,
                true
        );
        this.itemsDao.save(item);

        Item result = this.itemsDao.find(item.getId()).orElseThrow(() -> new RuntimeException("item entity not found"));

        assertEquals(item, result);

        assertEquals(1, this.productionInfoDao.findAll().count());
    }

    @Test
    public void whenCreateDuplicateItemThenCauseErrors() {

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

        User owner = getUserWithPhone("1");
        this.usersDao.save(owner);

        Car car = getCoupeCar(productionInfo);
        this.carDao.save(car);

        Item item = Item.create(
                ItemType.CAR,
                owner,
                car,
                true
        );
        Item duplicateItem = Item.create(
                ItemType.CAR,
                owner,
                car,
                true
        );
        this.itemsDao.save(item);

        Exception exception = assertThrows(ConstraintViolationException.class,
                () -> this.itemsDao.save(duplicateItem)
        );
        assertEquals("could not execute statement", exception.getMessage());
    }

    @Test
    public void whenUpdateItemThenStorageShouldContainTheUpdatedEntity() {

        Engine dieselEngine = getDieselEngine();
        Engine gasEngine = getGasEngine();
        this.engineDao.save(dieselEngine);
        this.engineDao.save(gasEngine);

        Transmission manualTransmission = getManualTransmission();
        Transmission autoTransmission = getAutoTransmission();
        this.transmissionDao.save(manualTransmission);
        this.transmissionDao.save(autoTransmission);

        ProductionInfo productionInfo1 = ProductionInfo.create(
                Make.AUDI,
                LocalDateTime.now().minusMonths(10),
                dieselEngine,
                manualTransmission,
                DriveType.ALL_WHEEL_DRIVE,
                Color.GREEN
        );
        ProductionInfo productionInfo2 = ProductionInfo.create(
                Make.ALFA_ROMEO,
                LocalDateTime.now().minusMonths(1),
                gasEngine,
                autoTransmission,
                DriveType.FRONT_WHEEL_DRIVE,
                Color.BLACK
        );
        this.productionInfoDao.save(productionInfo1);
        this.productionInfoDao.save(productionInfo2);

        User owner1 = getUserWithPhone("1");
        User owner2 = getUserWithPhone("2");
        this.usersDao.save(owner1);
        this.usersDao.save(owner2);

        Car car1 = getCoupeCar(productionInfo1);
        Car car2 = getHatchbackCar(productionInfo2);
        this.carDao.save(car1);
        this.carDao.save(car2);

        Item item = Item.create(
                ItemType.CAR,
                owner1,
                car1,
                true
        );
        this.itemsDao.save(item);

        Optional<Item> saved = this.itemsDao.find(item.getId());
        assertTrue(saved.isPresent());

        Photo photo = Photo.create("photo", new byte[]{1, 0, 1, 1, 1, 0, 0, 4, 1, 1});
        photoDao.save(photo);

        Photo savedPhoto = this.photoDao.find(photo.getId()).orElseThrow(() -> new RuntimeException("photo entity not found"));

        saved.map(itemToUpdate -> {
            itemToUpdate.setCar(car2);
            itemToUpdate.setCreatedBy(owner2);
            itemToUpdate.setType(ItemType.COMMERCIAL);
            itemToUpdate.setActive(false);
//            itemToUpdate.getPhotos().add(photo);
            itemToUpdate.addPhotoIds(Collections.singletonList(savedPhoto.getId()));
            return itemToUpdate;
        }).ifPresent(itemsDao::update);

        Item updated = this.itemsDao.find(saved.get().getId()).orElseThrow(() -> new RuntimeException("item entity not found"));

        assertEquals(car2, updated.getCar());
        assertEquals(owner2, updated.getCreatedBy());
        assertEquals(ItemType.COMMERCIAL, updated.getType());
        assertEquals(false, updated.getActive());
        assertEquals(photo.getId(), updated.getPhotoIds().iterator().next());

        assertEquals(1, this.itemsDao.findAll().count());
    }

    @Test
    public void whenDeleteItemThenStorageShouldNotContainTheEntity() {

        Engine dieselEngine = getDieselEngine();
        Engine gasEngine = getGasEngine();
        this.engineDao.save(dieselEngine);
        this.engineDao.save(gasEngine);

        Transmission manualTransmission = getManualTransmission();
        Transmission autoTransmission = getAutoTransmission();
        this.transmissionDao.save(manualTransmission);
        this.transmissionDao.save(autoTransmission);

        ProductionInfo productionInfo1 = ProductionInfo.create(
                Make.AUDI,
                LocalDateTime.now().minusMonths(10),
                dieselEngine,
                manualTransmission,
                DriveType.ALL_WHEEL_DRIVE,
                Color.GREEN
        );
        ProductionInfo productionInfo2 = ProductionInfo.create(
                Make.ALFA_ROMEO,
                LocalDateTime.now().minusMonths(1),
                gasEngine,
                autoTransmission,
                DriveType.FRONT_WHEEL_DRIVE,
                Color.BLACK
        );
        this.productionInfoDao.save(productionInfo1);
        this.productionInfoDao.save(productionInfo2);

        User owner1 = getUserWithPhone("1");
        User owner2 = getUserWithPhone("2");
        this.usersDao.save(owner1);
        this.usersDao.save(owner2);

        Car car1 = getCoupeCar(productionInfo1);
        Car car2 = getHatchbackCar(productionInfo2);
        this.carDao.save(car1);
        this.carDao.save(car2);

        Item item1 = Item.create(
                ItemType.CAR,
                owner1,
                car1,
                true
        );
        Item item2 = Item.create(
                ItemType.CAR,
                owner2,
                car2,
                true
        );
        this.itemsDao.save(item1);
        this.itemsDao.save(item2);

        assertEquals(2, this.itemsDao.findAll().count());
        assertTrue(this.itemsDao.find(item1.getId()).isPresent());
        assertTrue(this.itemsDao.find(item2.getId()).isPresent());

        this.itemsDao.delete(item1);

        assertTrue(this.itemsDao.find(item1.getId()).isEmpty());
        assertEquals(item2, this.itemsDao.find(item2.getId()).get());

        assertEquals(1, this.itemsDao.findAll().count());
    }

}