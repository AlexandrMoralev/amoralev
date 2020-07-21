package ru.job4j.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.domain.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public class ItemsDao {

    public static final ItemsDao INSTANCE = new ItemsDao();

    private final SessionFactory sessionFactory;

    private ItemsDao() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public Item addItem(Item item) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(item);
        session.getTransaction().commit();
        session.close();
        return item;
    }

    public Optional<Item> getItem(Integer id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Optional<Item> item = ofNullable(session.get(Item.class, id));
        session.getTransaction().commit();
        session.close();
        return item;
    }

    public void updateItem(Integer itemId) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        ofNullable(session.get(Item.class, itemId))
                .ifPresent(item -> {
                    item.setDone(true);
                    session.update(item);
                });
        session.getTransaction().commit();
        session.close();
    }

    public void deleteItem(Integer itemId) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Item deletableItem = new Item();
        deletableItem.setId(itemId);
        session.delete(deletableItem);
        session.getTransaction().commit();
        session.close();
    }

    public Collection<Item> getItems() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List result = session.createQuery("from ru.job4j.domain.Item").list();
        Collection<Item> items = new ArrayList<>(result);
        session.getTransaction().commit();
        session.close();
        return items;
    }

}
