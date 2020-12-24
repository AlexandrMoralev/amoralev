package ru.job4j.persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.TestAppContext;
import ru.job4j.entity.Transmission;
import ru.job4j.entity.enumerations.TransmissionType;
import ru.job4j.persistence.impl.TransmissionDao;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.job4j.TestUtils.*;

public class TransmissionDaoTest {

    private final TestAppContext context = TestAppContext.INSTANCE;
    private final TransmissionDao transmissionDao = context.transmissionDao;

    @AfterEach
    public void cleanUp() {
        this.transmissionDao.findAll().forEach(transmissionDao::delete);
    }

    @Test
    public void whenAddTransmissionThenStorageShouldContainTheSameEntity() {
        Transmission transmission = getAutoTransmission();

        this.transmissionDao.save(transmission);
        Optional<Transmission> result = this.transmissionDao.find(transmission.getId());

        assertEquals("transmission_model", result.get().getModel());
        assertEquals(TransmissionType.AUTOMATIC, result.get().getType());
    }

    @Test
    public void createDuplicateTransmissionDoesntCauseErrors() {
        Transmission transmission = getAutoTransmission();
        Transmission duplicate = getAutoTransmission();

        this.transmissionDao.save(transmission);
        this.transmissionDao.save(duplicate);
        Optional<Transmission> optionalTransmission = this.transmissionDao.find(transmission.getId());
        Optional<Transmission> optionalDuplicate = this.transmissionDao.find(duplicate.getId());

        assertEquals("transmission_model", optionalTransmission.get().getModel());
        assertEquals(TransmissionType.AUTOMATIC, optionalTransmission.get().getType());
        assertEquals(2, this.transmissionDao.findAll().size());
        assertTrue(optionalDuplicate.isPresent());
    }

    @Test
    public void whenUpdateTransmissionThenStorageShouldContainTheUpdatedEntity() {
        Transmission transmission1 = getAutoTransmission();
        Transmission transmission2 = getCvtTransmission();

        this.transmissionDao.save(transmission1);
        this.transmissionDao.save(transmission2);
        Optional<Transmission> saved1 = this.transmissionDao.find(transmission1.getId());
        Optional<Transmission> saved2 = this.transmissionDao.find(transmission2.getId());

        assertTrue(saved1.isPresent());
        assertTrue(saved2.isPresent());

        saved1.map(t -> {
            t.setType(TransmissionType.MANUAL);
            t.setModel("updated_transmission");
            return t;
        }).ifPresent(this.transmissionDao::update);

        Optional<Transmission> updated = this.transmissionDao.find(transmission1.getId());
        Optional<Transmission> notUpdated = this.transmissionDao.find(transmission2.getId());

        assertEquals("updated_transmission", updated.get().getModel());
        assertEquals(TransmissionType.MANUAL, updated.get().getType());

        assertEquals(saved2, notUpdated);
    }

    @Test
    public void whenDeleteTransmissionThenStorageShouldNotContainTheEntity() {
        Transmission transmission1 = getAutoTransmission();
        Transmission transmission2 = getManualTransmission();

        this.transmissionDao.save(transmission1);
        this.transmissionDao.save(transmission2);
        Optional<Transmission> result1 = this.transmissionDao.find(transmission1.getId());
        Optional<Transmission> result2 = this.transmissionDao.find(transmission2.getId());

        assertTrue(result1.isPresent());
        assertTrue(result2.isPresent());

        assertEquals(2, this.transmissionDao.findAll().size());

        this.transmissionDao.delete(transmission1);
        this.transmissionDao.deleteById(transmission2.getId());

        assertTrue(this.transmissionDao.find(transmission1.getId()).isEmpty());
        assertTrue(this.transmissionDao.find(transmission2.getId()).isEmpty());
        assertTrue(this.transmissionDao.findAll().isEmpty());
    }

}
