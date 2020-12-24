package ru.job4j.persistence.impl;

import ru.job4j.entity.Car;
import ru.job4j.persistence.AbstractDao;
import ru.job4j.util.HibernateUtils;

public class CarDao extends AbstractDao<Car> {

    public CarDao(HibernateUtils db) {
        super(db, Car.class);
    }
}

