package ru.job4j.mapping.modelsrelations.annotationbased.impl;

import ru.job4j.HibernateUtils;
import ru.job4j.mapping.modelsrelations.annotationbased.entity.Engine;
import ru.job4j.mapping.modelsrelations.persistence.dao.AbstractDao;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public class EngineDaoImpl extends AbstractDao<Engine> {

    public EngineDaoImpl(HibernateUtils hibernateUtils) {
        super(hibernateUtils);
    }

    @Override
    public Optional<Engine> getById(Long id) {
        return this.db.tx(
                session -> {
                    return ofNullable(session.get(Engine.class, id));
                }
        );
    }

    @Override
    public List<Engine> findAll() {
        return this.db.tx(
                session -> {
                    return session.createQuery("from ru.job4j.mapping.modelsrelations.annotationbased.entity.Engine").list();
                }
        );
    }
}

