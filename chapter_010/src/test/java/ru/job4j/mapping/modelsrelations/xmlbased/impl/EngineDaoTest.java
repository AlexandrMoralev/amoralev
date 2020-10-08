package ru.job4j.mapping.modelsrelations.xmlbased.impl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.HibernateUtils;
import ru.job4j.mapping.modelsrelations.TestAppContext;
import ru.job4j.mapping.modelsrelations.persistence.dao.Dao;
import ru.job4j.mapping.modelsrelations.xmlbased.entity.Engine;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EngineDaoTest {

    private final HibernateUtils db;
    private final Dao<Engine> engineDao;

    private static Engine enginePowerfull;
    private static Engine engineCheap;

    public EngineDaoTest() {
        this.db = TestAppContext.createXmlBasedUtils();
        engineDao = new EngineDaoImpl(db);
    }

    @BeforeAll
    public static void init() {
        enginePowerfull = Engine.create("qwert", 200, 100);
        engineCheap = Engine.create("zxc", 20, 10);
    }

    @Test
    public void saveEngineTest() {

        assertTrue(engineDao.findAll().isEmpty());

        engineDao.save(enginePowerfull);
        engineDao.save(engineCheap);

        assertEquals(engineDao.getById(enginePowerfull.getId()).get(), enginePowerfull);
        assertEquals(engineDao.getById(engineCheap.getId()).get(), engineCheap);

        assertTrue(engineDao.findAll().containsAll(List.of(engineCheap, enginePowerfull)));
    }

    @Test
    public void updateEngineTest() {

        assertTrue(engineDao.findAll().isEmpty());

        engineDao.save(enginePowerfull);
        engineDao.save(engineCheap);

        enginePowerfull.setHp(300);
        enginePowerfull.setCost(500);
        engineDao.update(enginePowerfull);

        Engine updated = engineDao.getById(enginePowerfull.getId()).get();
        assertEquals(updated, enginePowerfull);
        assertEquals("qwert", updated.getModel());
        assertEquals(300, updated.getHp());
        assertEquals(500, updated.getCost());

        assertEquals(engineDao.getById(engineCheap.getId()).get(), engineCheap);

        assertTrue(engineDao.findAll().containsAll(List.of(engineCheap, enginePowerfull)));
    }

    @Test
    public void deleteEngineTest() {

        assertTrue(engineDao.findAll().isEmpty());

        engineDao.save(enginePowerfull);
        engineDao.save(engineCheap);

        engineDao.delete(engineCheap);

        assertEquals(engineDao.getById(enginePowerfull.getId()).get(), enginePowerfull);

        assertTrue(engineDao.getById(engineCheap.getId()).isEmpty());

        List<Engine> engines = engineDao.findAll();
        assertTrue(engines.contains(enginePowerfull));
        assertFalse(engines.contains(engineCheap));
    }


}

