package ru.job4j.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.query.Query;
import ru.job4j.domain.Item;
import ru.job4j.util.HibernateUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * ItemsDaoImpl
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class ItemsDaoImpl implements Dao<Item> {

    private static final Logger LOG = LogManager.getLogger(ItemsDaoImpl.class);
    private final HibernateUtils daoUtils;

    public ItemsDaoImpl(HibernateUtils daoUtils) {
        this.daoUtils = daoUtils;
        LOG.info("ItemsDaoImpl created");
    }

    @Override
    public Item save(Item item) {
        return daoUtils.tx(
                session -> {
                    session.save(item);
                    return item;
                }
        );
    }

    @Override
    public Optional<Item> findById(Integer id) {
        return daoUtils.tx(
                session -> {
                    return ofNullable(session.get(Item.class, id));
                }
        );
    }

    @Override
    public List<Item> findByCriteria(String fieldName, String fieldValue) {
        return daoUtils.tx(
                session -> {
                    CriteriaBuilder builder = session.getCriteriaBuilder();
                    CriteriaQuery<Item> query = builder.createQuery(Item.class);
                    Root<Item> root = query.from(Item.class);
                    query.select(root).where(builder.like(root.get(fieldName), "%" + fieldValue + "%")); // FIXME type matching
                    Query<Item> q = session.createQuery(query);
                    return q.getResultList();
                }
        );
    }

    @Override
    public void update(Item updatedItem) {
        daoUtils.tx(
                session -> {
                    ofNullable(session.get(Item.class, updatedItem.getId()))
                            .ifPresent(item -> session.update(updatedItem));
                }
        );
    }

    @Override
    public void delete(Integer itemId) {
        daoUtils.tx(
                session -> {
                    ofNullable(session.get(Item.class, itemId))
                            .ifPresent(session::delete);
                }
        );
    }

    @Override
    public List<Item> findAll() {
        return daoUtils.tx(
                session -> {
                    return session.createQuery("from ru.job4j.domain.Item").list();
                }
        );
    }

}
