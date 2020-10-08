package ru.job4j.mapping.modelsrelations.annotationbased.impl;

import ru.job4j.HibernateUtils;
import ru.job4j.mapping.modelsrelations.annotationbased.entity.Driver;
import ru.job4j.mapping.modelsrelations.persistence.dao.AbstractDao;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public class DriverDaoImpl extends AbstractDao<Driver> {

    public DriverDaoImpl(HibernateUtils hibernateUtils) {
        super(hibernateUtils);
    }

    @Override
    public Optional<Driver> getById(Long id) {
        return this.db.tx(
                session -> {
                    return ofNullable(session.get(Driver.class, id));
                }
        );
    }

    @Override
    public List<Driver> findAll() {
        return this.db.tx(
                session -> {
                    return session.createQuery("from ru.job4j.mapping.modelsrelations.annotationbased.entity.Driver").list();
                }
        );
    }

}