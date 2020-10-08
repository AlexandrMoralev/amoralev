package ru.job4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.function.Consumer;
import java.util.function.Function;

public class HibernateUtils {

    private final Logger logger = LogManager.getLogger(HibernateUtils.class);

    private SessionFactory sessionFactory;

    public HibernateUtils(SessionFactory sessionFactory) { // configFilename
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    public void shutdown() {
        this.sessionFactory.close();
    }

    public <T> T tx(final Function<Session, T> command) {
        final Session session = this.sessionFactory.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public void tx(final Consumer<Session> command) {
        final Session session = this.sessionFactory.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            command.accept(session);
            tx.commit();
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

}
