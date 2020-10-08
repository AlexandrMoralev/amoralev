package ru.job4j.mapping.modelsrelations.xmlbased.impl;

import ru.job4j.HibernateUtils;
import ru.job4j.mapping.modelsrelations.persistence.dao.AbstractDao;
import ru.job4j.mapping.modelsrelations.xmlbased.entity.Car;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public class CarDaoImpl extends AbstractDao<Car> {

    public CarDaoImpl(HibernateUtils hibernateUtils) {
        super(hibernateUtils);
    }

    @Override
    public Optional<Car> getById(Long id) {
        return this.db.tx(
                session -> {
                    return ofNullable(session.get(Car.class, id));
                }
        );
    }

    @Override
    public List<Car> findAll() {
        return this.db.tx(
                session -> {
                    return session.createQuery("from ru.job4j.mapping.modelsrelations.xmlbased.entity.Car").list();
                }
        );
    }

}
