package ru.job4j.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.ui.dto.FilterInfo;
import ru.job4j.util.HibernateUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * AbstractDao
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public abstract class AbstractDao<T extends Serializable> {

    protected final static Logger LOG = LogManager.getLogger(AbstractDao.class);

    protected final HibernateUtils db;

    protected final Class<T> clazz;

    public AbstractDao(HibernateUtils db,
                       Class<T> clazz
    ) {
        this.clazz = clazz;
        this.db = db;
    }

    public Optional<T> find(long id) {
        return db.tx(
                session -> {
                    return ofNullable(session.get(clazz, id));
                }
        );
    }

    public Optional<T> find(int id) {
        return db.tx(
                session -> {
                    return ofNullable(session.get(clazz, id));
                }
        );
    }

    public Collection<T> findAll() {
        return db.tx(
                session -> {
                    return session.createQuery("from " + clazz.getName()).list();
                }
        );
    }

    public Collection<T> findAll(FilterInfo filter) {
        return findAll();
    }

    public void save(T entity) {
        db.tx(
                session -> {
                    session.saveOrUpdate(entity);
                }
        );
    }

    public void update(T entity) {
        db.tx(
                session -> {
                    session.update(entity);
                }
        );
    }

    public void delete(T entity) {
        db.tx(
                session -> {
                    session.delete(entity);
                }
        );
    }

    public void deleteById(long id) {
        db.tx(
                session -> {
                    find(id).ifPresent(session::delete);
                }
        );
    }

    public void deleteById(int id) {
        db.tx(
                session -> {
                    find(id).ifPresent(session::delete);
                }
        );
    }

}
