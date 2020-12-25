package ru.job4j.persistence.impl;

import ru.job4j.entity.Engine;
import ru.job4j.persistence.AbstractDao;
import ru.job4j.util.HibernateUtils;

public class EngineDao extends AbstractDao<Engine> {

    public EngineDao(HibernateUtils db) {
        super(db, Engine.class);
    }
}
