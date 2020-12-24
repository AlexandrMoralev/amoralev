package ru.job4j.persistence;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.TestAppContext;
import ru.job4j.entity.Engine;
import ru.job4j.entity.enumerations.EngineType;
import ru.job4j.persistence.impl.EngineDao;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static ru.job4j.TestUtils.*;

public class EngineDaoTest {

    private final TestAppContext context = TestAppContext.INSTANCE;
    private final EngineDao engineDao = context.engineDao;

    @AfterEach
    public void cleanUp() {
        this.engineDao.findAll().forEach(engineDao::delete);
    }

    @Test
    public void whenAddEngineThenStorageShouldContainTheSameEntity() {
        Engine engine = getDieselEngine();

        this.engineDao.save(engine);
        Engine result = this.engineDao.find(engine.getId()).orElseThrow(() -> new RuntimeException("engine entity not found"));

        assertEquals(engine, result);
        assertEquals(1, this.engineDao.findAll().size());
    }

    @Test
    public void createDuplicateEngineCauseErrors() {
        Engine engine = getDieselEngine();
        Engine duplicate = getDieselEngine();

        this.engineDao.save(engine);

        Exception exception = assertThrows(ConstraintViolationException.class,
                () -> this.engineDao.save(duplicate)
        );
        assertEquals("could not execute statement", exception.getMessage());
    }

    @Test
    public void whenUpdateEngineThenStorageShouldContainTheUpdatedEntity() {
        Engine engine1 = getDieselEngine();
        Engine engine2 = getElectricEngine();

        this.engineDao.save(engine1);
        this.engineDao.save(engine2);
        Optional<Engine> saved1 = this.engineDao.find(engine1.getId());
        Optional<Engine> saved2 = this.engineDao.find(engine2.getId());

        assertTrue(saved1.isPresent());
        assertTrue(saved2.isPresent());

        saved1.map(engine -> {
            engine.setHp(180);
            engine.setModel("hybrid engine model");
            engine.setVolume(2600);
            engine.setType(EngineType.HYBRID);
            return engine;
        }).ifPresent(this.engineDao::update);

        Engine updated = this.engineDao.find(engine1.getId()).orElseThrow(() -> new RuntimeException("engine entity not found"));
        Engine notUpdated = this.engineDao.find(engine2.getId()).orElseThrow(() -> new RuntimeException("engine entity not found"));

        assertEquals("hybrid engine model", updated.getModel());
        assertEquals("electric engine model", notUpdated.getModel());

        assertEquals(EngineType.HYBRID, updated.getType());
        assertEquals(EngineType.ELECTRIC, notUpdated.getType());

        assertEquals(180, updated.getHp());
        assertEquals(1200, notUpdated.getHp());

        assertEquals(2600, updated.getVolume());
        assertEquals(0, notUpdated.getVolume());
    }

    @Test
    public void whenDeleteEngineThenStorageShouldNotContainTheEntity() {
        Engine engine1 = getDieselEngine();
        Engine engine2 = getGasEngine();
        Engine engine3 = getElectricEngine();

        this.engineDao.save(engine1);
        this.engineDao.save(engine2);
        this.engineDao.save(engine3);

        assertTrue(this.engineDao.find(engine1.getId()).isPresent());
        assertTrue(this.engineDao.find(engine2.getId()).isPresent());

        Optional<Engine> engine = this.engineDao.find(engine3.getId());
        assertTrue(engine.isPresent());

        assertEquals(3, this.engineDao.findAll().size());

        this.engineDao.delete(engine1);
        this.engineDao.deleteById(engine2.getId());

        assertTrue(this.engineDao.find(engine1.getId()).isEmpty());
        assertTrue(this.engineDao.find(engine2.getId()).isEmpty());
        assertEquals(engine.get(), this.engineDao.findAll().iterator().next());
    }

}
