package ru.job4j.mapping.modelsrelations.xmlbased.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.HibernateUtils;
import ru.job4j.mapping.modelsrelations.TestAppContext;
import ru.job4j.mapping.modelsrelations.persistence.dao.Dao;
import ru.job4j.mapping.modelsrelations.xmlbased.entity.Driver;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DriverDaoTest {

    private final HibernateUtils db;
    private final Dao<Driver> driverDao;

    private Driver driverFirst;
    private Driver driverSecond;


    public DriverDaoTest() {
        this.db = TestAppContext.createXmlBasedUtils();
        driverDao = new DriverDaoImpl(db);
    }

    @BeforeEach
    public void init() {
        driverFirst = new Driver("qwert");
        driverSecond = new Driver("zxc");
    }

    @Test
    public void saveDriverTest() {

        assertTrue(driverDao.findAll().isEmpty());

        driverDao.save(driverFirst);
        driverDao.save(driverSecond);

        assertEquals(driverDao.getById(driverFirst.getId()).get(), driverFirst);
        assertEquals(driverDao.getById(driverSecond.getId()).get(), driverSecond);

        assertTrue(driverDao.findAll().containsAll(List.of(driverSecond, driverFirst)));
    }

    @Test
    public void updateDriverTest() {

        assertTrue(driverDao.findAll().isEmpty());

        driverDao.save(driverFirst);
        driverDao.save(driverSecond);

        driverFirst.setName("ASD");

        driverDao.update(driverFirst);

        Driver updated = driverDao.getById(driverFirst.getId()).get();

        assertEquals(updated, driverFirst);
        assertEquals("ASD", updated.getName());

        assertEquals(driverDao.getById(driverSecond.getId()).get(), driverSecond);

        assertTrue(driverDao.findAll().containsAll(List.of(driverSecond, driverFirst)));
    }

    @Test
    public void deleteDriverTest() {

        assertTrue(driverDao.findAll().isEmpty());

        driverDao.save(driverFirst);
        driverDao.save(driverSecond);

        driverDao.delete(driverSecond);

        assertEquals(driverDao.getById(driverFirst.getId()).get(), driverFirst);

        assertTrue(driverDao.getById(driverSecond.getId()).isEmpty());

        List<Driver> engines = driverDao.findAll();
        assertTrue(engines.contains(driverFirst));
        assertFalse(engines.contains(driverSecond));
    }


}

