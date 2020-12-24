package ru.job4j.persistence.impl;

import org.hibernate.query.Query;
import ru.job4j.entity.User;
import ru.job4j.persistence.AbstractDao;
import ru.job4j.util.HibernateUtils;

import java.util.Optional;

import static java.util.Optional.ofNullable;

public class UsersDao extends AbstractDao<User> {

    public UsersDao(HibernateUtils db) {
        super(db, User.class);
    }

    public Optional<User> findByPhone(String phone) {
        return db.tx(
                session -> {
                    Query<User> query = session.createQuery("from User u where u.phone=:phone", clazz);
                    query.setParameter("phone", phone);
                    return query.getResultStream().findFirst();
                }
        );
    }

    @Override
    public void delete(User user) {
        db.tx(
                session -> {
                    ofNullable(session.get(User.class, user.getId())).ifPresent(session::delete);
                }
        );
    }

    @Override
    public void deleteById(int id) {
        db.tx(
                session -> {
                    ofNullable(session.get(User.class, id)).ifPresent(session::delete);
                }
        );
    }
}
