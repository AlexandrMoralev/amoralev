package ru.job4j.templatedata.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.mvc.model.Accident;
import ru.job4j.mvc.repository.AccidentStore;

import java.util.Collection;
import java.util.Optional;

@Repository
public class AccidentHibernate implements AccidentStore {

    private final SessionFactory sessionFactory;

    public AccidentHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Accident> getAccident(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return session
                    .createQuery("from ru.job4j.mvc.model.Accident acc where acc.id =: id", Accident.class)
                    .setParameter("id", id)
                    .getResultStream()
                    .findFirst();
        }
    }

    @Override
    public Collection<Accident> getAllAccidents() {
        try (Session session = sessionFactory.openSession()) {
            return session
                    .createQuery("from ru.job4j.mvc.model.Accident", Accident.class)
                    .list();
        }
    }

    @Override
    public Accident addAccident(Accident accident) {
        try (Session session = sessionFactory.openSession()) {
            session.save(accident);
            return accident;
        }
    }

    @Override
    public void updateAccident(Accident updatedAccident) {
        try (Session session = sessionFactory.openSession()) {
            session.update(updatedAccident);
        }
    }

    @Override
    public void removeAccident(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            session.createQuery("delete from ru.job4j.mvc.model.Accident acc where acc.id =: id", Accident.class)
                    .setParameter("id", id)
                    .executeUpdate();
        }
    }
}
