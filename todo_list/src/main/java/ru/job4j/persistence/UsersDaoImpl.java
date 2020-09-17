package ru.job4j.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.query.Query;
import ru.job4j.domain.User;
import ru.job4j.util.HibernateUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * UsersDaoImpl
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class UsersDaoImpl implements Dao<User> {

    private static final Logger LOG = LogManager.getLogger(UsersDaoImpl.class);
    private final HibernateUtils daoUtils;

    public UsersDaoImpl(HibernateUtils daoUtils) {
        this.daoUtils = daoUtils;
        LOG.info("UsersDaoImpl created");
    }

    @Override
    public User save(User user) {
        return daoUtils.tx(
                session -> {
                    session.save(user);
                    return user;
                }
        );
    }

    @Override
    public Optional<User> findById(Integer id) {
        return daoUtils.tx(
                session -> {
                    return ofNullable(session.get(User.class, id));
                }
        );
    }

    @Override
    public List<User> findByCriteria(String fieldName, String fieldValue) {
        return daoUtils.tx(
                session -> {
                    CriteriaBuilder builder = session.getCriteriaBuilder();
                    CriteriaQuery<User> query = builder.createQuery(User.class);
                    Root<User> root = query.from(User.class);
                    query.select(root).where(builder.equal(root.get(fieldName), fieldValue)); // FIXME type matching or specify method - byLogin
                    Query<User> q = session.createQuery(query);
                    return q.list();
                }
        );
    }

    @Override
    public void update(User user) {
        daoUtils.tx(
                session -> {
                    ofNullable(session.get(User.class, user.getId()))
                            .ifPresent(u -> session.merge(user));
                }
        );
    }

    @Override
    public void delete(Integer userId) {
        daoUtils.tx(
                session -> {
                    ofNullable(session.get(User.class, userId))
                            .ifPresent(session::remove);
                }
        );
    }

    @Override
    public List<User> findAll() {
        return daoUtils.tx(
                session -> {
                    return session.createQuery("from ru.job4j.domain.User").list();
                }
        );
    }

}
