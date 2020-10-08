package ru.job4j.mapping.modelsrelations.annotationbased.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.HibernateUtils;
import ru.job4j.mapping.modelsrelations.TestAppContext;
import ru.job4j.mapping.modelsrelations.annotationbased.entity.Engine;
import ru.job4j.mapping.modelsrelations.persistence.dao.Dao;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EngineDaoTest {

    private final HibernateUtils db;
    private final Dao<Engine> engineDao;

    private Engine enginePowerfull;
    private Engine engineCheap;


    public EngineDaoTest() {
        this.db = TestAppContext.createAnnotationBasedUtils();
        engineDao = new EngineDaoImpl(db);
    }

    @BeforeEach
    public void init() {
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

