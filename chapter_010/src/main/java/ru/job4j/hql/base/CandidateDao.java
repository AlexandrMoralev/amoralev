package ru.job4j.hql.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.query.Query;
import ru.job4j.HibernateUtils;

import java.util.Collection;
import java.util.Optional;

public class CandidateDao {

    private final static Logger LOG = LogManager.getLogger(CandidateDao.class);

    private final HibernateUtils db;

    public CandidateDao(HibernateUtils db) {
        this.db = db;
    }

    public Optional<Candidate> findById(long id) {
        return db.tx(
                session -> {
                    Query query = session.createQuery("from Candidate c where c.id=:cId", Candidate.class);
                    query.setParameter("cId", id);
                    return query.uniqueResultOptional();
                }
        );
    }

    public Optional<Candidate> findByName(String name) {
        return db.tx(
                session -> {
                    Query query = session.createQuery("from Candidate c where c.name=:cName", Candidate.class);
                    query.setParameter("cName", name);
                    return query.uniqueResultOptional();
                }
        );
    }

    public Collection<Candidate> findAll() {
        return db.tx(
                session -> {
                    return session.createQuery("from Candidate").list();
                }
        );
    }

    public void insert(Candidate entity) {
        db.tx(
                session -> {
                    session.createQuery("insert into Candidate (name, experience, salary) "
                            + "select concat(c.name, '_' +  c.id), concat(c.experience, '_' + c.id + 1), c.salary "
                            + "from Candidate c where c.id=:cId")
                            .setParameter("cId", entity.getId())
                            .executeUpdate();
                }
        );
    }

    public void save(Candidate entity) {
        db.tx(
                session -> {
                    session.save(entity);
                }
        );
    }

    public void update(Candidate entity) {
        db.tx(
                session -> {
                    session.createQuery("update Candidate c set c.name=:cName, c.experience=:cExperience, c.salary=:cSalary where c.id=:cId")
                            .setParameter("cName", entity.getName())
                            .setParameter("cExperience", entity.getExperience())
                            .setParameter("cSalary", entity.getSalary())
                            .setParameter("cId", entity.getId())
                            .executeUpdate();
                }
        );
    }

    public void deleteById(long id) {
        db.tx(
                session -> {
                    session.createQuery("delete from Candidate c where c.id=:cId")
                            .setParameter("cId", id)
                            .executeUpdate();
                }
        );
    }

}
