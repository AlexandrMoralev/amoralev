package ru.job4j.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.domain.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Optional.ofNullable;

public class ItemsDao {

    public static final ItemsDao INSTANCE = new ItemsDao(new StandardServiceRegistryBuilder().configure().build());

    private final SessionFactory sessionFactory;

    ItemsDao(StandardServiceRegistry registry) {
        sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public Item addItem(Item item) {
        return this.tx(
                session -> {
                    session.save(item);
                    return item;
                }
        );
    }

    public Optional<Item> getItem(Integer id) {
        return this.tx(
                session -> {
                    return ofNullable(session.get(Item.class, id));
                }
        );
    }

    public void updateItem(Integer itemId) {
        this.tx(
                session -> {
                    ofNullable(session.get(Item.class, itemId))
                            .ifPresent(item -> {
                                item.setDone(true);
                                session.update(item);
                            });
                }
        );
    }

    public void deleteItem(Integer itemId) {
        this.tx(
                session -> {
                    ofNullable(session.get(Item.class, itemId))
                            .ifPresent(session::delete);
                }
        );
    }

    public Collection<Item> getItems() {
        return this.tx(
                session -> {
                    final List result = session.createQuery("from ru.job4j.domain.Item").list();
                    return new ArrayList<>(result);
                }
        );
    }

    private <T> T tx(final Function<Session, T> command) {
        final Session session = sessionFactory.openSession();
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

    private void tx(final Consumer<Session> command) {
        final Session session = sessionFactory.openSession();
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
