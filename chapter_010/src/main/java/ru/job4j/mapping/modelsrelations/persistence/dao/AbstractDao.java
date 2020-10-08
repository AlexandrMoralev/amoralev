package ru.job4j.mapping.modelsrelations.persistence.dao;

import ru.job4j.HibernateUtils;

public abstract class AbstractDao<T> implements Dao<T> {

    protected final HibernateUtils db;

    public AbstractDao(HibernateUtils hibernateUtils) {
        this.db = hibernateUtils;
    }

    @Override
    public void save(T e) {
        this.db.tx(
                session -> {
                    session.save(e);
                }
        );
    }

    @Override
    public void update(T e) {
        this.db.tx(
                session -> {
                    session.update(e);
                }
        );
    }

    @Override
    public void delete(T e) {
        this.db.tx(
                session -> {
                    session.delete(e);
                }
        );
    }
}
