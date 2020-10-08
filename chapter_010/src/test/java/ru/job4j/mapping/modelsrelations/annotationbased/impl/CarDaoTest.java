package ru.job4j.mapping.modelsrelations.annotationbased.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.HibernateUtils;
import ru.job4j.mapping.modelsrelations.TestAppContext;
import ru.job4j.mapping.modelsrelations.annotationbased.entity.Car;
import ru.job4j.mapping.modelsrelations.annotationbased.entity.Driver;
import ru.job4j.mapping.modelsrelations.annotationbased.entity.Engine;
import ru.job4j.mapping.modelsrelations.persistence.dao.Dao;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CarDaoTest {

    private final HibernateUtils db;
    private final Dao<Car> carDao;
    private final Dao<Engine> engineDao;
    private final Dao<Driver> driverDao;

    private Engine enginePowerfull = Engine.create("qwert", 200, 100);
    private Engine engineCheap = Engine.create("zxc", 20, 10);
    private Engine engineBroken = Engine.create("asd", 0, 10);

    private Driver driverFirst = new Driver("qwert");
    private Driver driverSecond = new Driver("zxc");

    private final List<Engine> engines = List.of(engineCheap, enginePowerfull, engineBroken);
    private final List<Driver> drivers = List.of(driverFirst, driverSecond);

    private Car carFirst;
    private Car carSecond;

    public CarDaoTest() {
        this.db = TestAppContext.createAnnotationBasedUtils();
        carDao = new CarDaoImpl(db);
        engineDao = new EngineDaoImpl(db);
        driverDao = new DriverDaoImpl(db);
    }

    @BeforeEach
    public void init() {
        engines.forEach(engineDao::save);
        drivers.forEach(driverDao::save);
    }

    @Test
    public void saveCarTest() {

        assertTrue(carDao.findAll().isEmpty());

        carFirst = Car.create("qwert", enginePowerfull, drivers);
        carSecond = Car.create("zxc", engineCheap, Collections.emptyList());

        carDao.save(carFirst);
        carDao.save(carSecond);

        Car updatedFirst = carDao.getById(carFirst.getId()).get();
        Car updatedSecond = carDao.getById(carSecond.getId()).get();

        assertEquals(updatedFirst, carFirst);
        assertEquals(updatedFirst.getEngine(), enginePowerfull);
        assertTrue(updatedFirst.getOwners().containsAll(drivers));

        assertEquals(updatedSecond, carSecond);
        assertEquals(updatedSecond.getEngine(), engineCheap);
        assertTrue(updatedSecond.getOwners().isEmpty());

        assertTrue(carDao.findAll().containsAll(List.of(carSecond, carFirst)));
    }

    @Test
    public void updateCarTest() {

        assertTrue(carDao.findAll().isEmpty());

        carFirst = Car.create("qwert", enginePowerfull, drivers);
        carSecond = Car.create("zxc", engineCheap, Collections.emptyList());

        carDao.save(carFirst);
        carDao.save(carSecond);

        carFirst.setBrand("ASD");
        carFirst.setEngine(engineBroken);
        carFirst.setOwners(new HashSet<>());

        carSecond.getOwners().addAll(drivers);

        carDao.update(carFirst);
        carDao.update(carSecond);

        Car updatedFirst = carDao.getById(carFirst.getId()).get();
        Car updatedSecond = carDao.getById(carSecond.getId()).get();

        assertEquals(updatedFirst, carFirst);
        assertEquals(updatedFirst.getBrand(), "ASD");
        assertEquals(updatedFirst.getEngine(), engineBroken);
        assertTrue(updatedFirst.getOwners().isEmpty());

        assertEquals(updatedSecond, carSecond);
        assertEquals(updatedSecond.getEngine(), engineCheap);
        assertTrue(updatedSecond.getOwners().containsAll(drivers));

        assertTrue(carDao.findAll().containsAll(List.of(carSecond, carFirst)));
    }

    @Test
    public void deleteCarTest() {

        assertTrue(carDao.findAll().isEmpty());

        carFirst = Car.create("qwert", enginePowerfull, drivers);
        carSecond = Car.create("zxc", engineCheap, Collections.emptyList());

        carDao.save(carFirst);
        carDao.save(carSecond);

        carDao.delete(carFirst);

        assertTrue(carDao.getById(carFirst.getId()).isEmpty());
        assertTrue(carDao.getById(carSecond.getId()).isPresent());

        List<Car> cars = carDao.findAll();
        assertEquals(cars.size(), 1);
        assertTrue(cars.contains(carSecond));

        assertTrue(engineDao.getById(enginePowerfull.getId()).isPresent());
        assertTrue(driverDao.findAll().isEmpty());
    }


}

