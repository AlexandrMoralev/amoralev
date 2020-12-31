package ru.job4j.persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.job4j.TestAppContext;
import ru.job4j.entity.*;
import ru.job4j.entity.enumerations.Color;
import ru.job4j.entity.enumerations.DriveType;
import ru.job4j.entity.enumerations.ItemType;
import ru.job4j.entity.enumerations.Make;
import ru.job4j.persistence.impl.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.job4j.TestUtils.*;

@Disabled
public class UsersDaoTest {

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
    public void whenAddUserThenStorageShouldContainTheSameEntity() {

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

        User user = getUserWithPhone("1");
        this.usersDao.save(user);

        Item item = Item.create(
                ItemType.CAR,
                user,
                car,
                true
        );
        this.itemsDao.save(item);

        user.addItem(item);
        this.usersDao.update(user);

        User result = this.usersDao.find(user.getId()).orElseThrow(() -> new RuntimeException("user entity not found"));

        assertEquals(user, result);
        assertTrue(user.getItems().contains(item));
        assertEquals(car, user.getItems().iterator().next().getCar());

        assertEquals(1, this.usersDao.findAll().count());
    }

    @Test
    public void whenUpdateProductionInfoThenStorageShouldContainTheUpdatedEntity() {
        User user1 = getUserWithPhone("1");
        User user2 = getUserWithPhone("2");
        this.usersDao.save(user1);
        this.usersDao.save(user2);

        Optional<User> saved1 = this.usersDao.find(user1.getId());
        Optional<User> saved2 = this.usersDao.find(user2.getId());
        assertTrue(saved1.isPresent());
        assertTrue(saved2.isPresent());
        assertEquals(2, this.usersDao.findAll().count());

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
        Item item = Item.create(
                ItemType.CAR,
                saved1.get(),
                car,
                true
        );
        this.itemsDao.save(item);

        saved1.map(u -> {
            u.setName("updated name");
            u.setPhone("updated phone");
            u.setPassword("updated pwd");
            u.setSalt("updated salt");
            u.getItems().add(item);
            return u;
        }).ifPresent(usersDao::update);

        User updated = this.usersDao.find(saved1.get().getId()).orElseThrow(() -> new RuntimeException("user entity not found"));

        assertEquals("updated name", updated.getName());
        assertEquals("updated phone", updated.getPhone());
        assertEquals("updated pwd", updated.getPassword());
        assertEquals("updated salt", updated.getSalt());
        assertEquals(item, updated.getItems().iterator().next());

        assertEquals(user2, this.usersDao.find(user2.getId()).get());
        assertEquals(2, this.usersDao.findAll().count());
    }


    @Test
    public void whenDeleteUserThenStorageShouldNotContainTheEntity() {

        User user1 = getUserWithPhone("1");
        User user2 = getUserWithPhone("2");
        this.usersDao.save(user1);
        this.usersDao.save(user2);

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
        Car car1 = getCoupeCar(productionInfo);
        Car car2 = getHatchbackCar(productionInfo);
        this.carDao.save(car1);
        this.carDao.save(car2);

        Item item1 = Item.create(
                ItemType.CAR,
                user1,
                car1,
                true
        );
        Item item2 = Item.create(
                ItemType.CAR,
                user2,
                car2,
                true
        );
        this.itemsDao.save(item1);
        this.itemsDao.save(item2);

        assertTrue(this.usersDao.find(user1.getId()).isPresent());
        assertTrue(this.usersDao.find(user2.getId()).isPresent());
        assertEquals(2, this.usersDao.findAll().count());

        this.usersDao.delete(user1);
        this.usersDao.deleteById(user2.getId());

        assertTrue(this.usersDao.find(user1.getId()).isEmpty());
        assertTrue(this.usersDao.find(user2.getId()).isEmpty());
        assertEquals(0, this.usersDao.findAll().count());

        assertTrue(this.itemsDao.find(item1.getId()).isEmpty());
        assertTrue(this.itemsDao.find(item2.getId()).isEmpty());

        assertTrue(this.carDao.find(car1.getId()).isEmpty());
        assertTrue(this.carDao.find(car2.getId()).isEmpty());
    }
}
