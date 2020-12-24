package ru.job4j.persistence.impl;

import ru.job4j.entity.Transmission;
import ru.job4j.persistence.AbstractDao;
import ru.job4j.util.HibernateUtils;

public class TransmissionDao extends AbstractDao<Transmission> {

    public TransmissionDao(HibernateUtils db) {
        super(db, Transmission.class);
    }
}
