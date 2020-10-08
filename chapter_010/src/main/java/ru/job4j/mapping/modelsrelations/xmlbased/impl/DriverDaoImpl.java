package ru.job4j.mapping.modelsrelations.xmlbased.impl;

import ru.job4j.HibernateUtils;
import ru.job4j.mapping.modelsrelations.persistence.dao.AbstractDao;
import ru.job4j.mapping.modelsrelations.xmlbased.entity.Driver;

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
                    return ofNullable(session.find(Driver.class, id));
                }
        );
    }

    @Override
    public List<Driver> findAll() {
        return this.db.tx(
                session -> {
                    return session.createQuery("from ru.job4j.mapping.modelsrelations.xmlbased.entity.Driver").list();
                }
        );
    }

}
