package ru.job4j.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.domain.Item;

import java.util.Collection;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemoryItemsDaoTest {

    public static ItemsDao itemsDao;

    private static SessionFactory sessionFactory;

    private static Item firstItem;
    private static Item secondItem;
    private static Item thirdItem;

    @BeforeEach
    public void beforeAll() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("test.hibernate.cfg.xml").build();
        itemsDao = new ItemsDao(registry);
        sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        firstItem = createItem(1, "first description", System.currentTimeMillis(), false);
        secondItem = createItem(2, "second description", System.currentTimeMillis(), true);
        thirdItem = createItem(null, "third description", 0L, false);
    }


    @Test
    public void addItemTest() {
        Item item = itemsDao.addItem(firstItem);
        assertEquals(firstItem, item);
        assertEquals(firstItem, itemsDao.getItem(firstItem.getId()).orElseGet(Item::new));
        assertTrue(itemsDao.getItem(secondItem.getId()).isEmpty());
    }

    @Test
    public void getItemTest() {
        addItems(firstItem, secondItem);
        assertEquals(firstItem, itemsDao.getItem(firstItem.getId()).orElseGet(Item::new));
        assertEquals(secondItem, itemsDao.getItem(secondItem.getId()).orElseGet(Item::new));
    }

    @Test
    public void updateItemTest() {
        Item controlItem = itemsDao.addItem(firstItem);
        Item updateableItem = itemsDao.addItem(thirdItem);
        itemsDao.updateItem(updateableItem.getId());
        Item result = itemsDao.getItem(updateableItem.getId()).orElseGet(Item::new);
        assertEquals(controlItem, itemsDao.getItem(controlItem.getId()).orElseGet(Item::new));
        assertTrue(result.isDone());
    }

    @Test
    public void deleteItemTest() {
        Item first = itemsDao.addItem(firstItem);
        Item second = itemsDao.addItem(secondItem);
        Item third = itemsDao.addItem(thirdItem);
        itemsDao.deleteItem(first.getId());
        itemsDao.deleteItem(second.getId());
        Collection<Item> storedItems = itemsDao.getItems();
        assertEquals(1, storedItems.size());
        assertTrue(storedItems.stream().allMatch(third::equals));
    }

    @Test
    public void getAllItemsTest() {
        addItems(firstItem, secondItem, thirdItem);
        Collection<Item> storedItems = itemsDao.getItems();
        assertEquals(3, storedItems.size());
    }

    @AfterEach
    public void cleanUp() {
        final Session session = sessionFactory.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            session.clear();
            tx.commit();
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    private static Item createItem(Integer id, String description, long createdInMillis, boolean isDone) {
        Item item = new Item();
        item.setId(id);
        item.setDescription(description);
        item.setCreated(createdInMillis);
        item.setDone(isDone);
        return item;
    }

    private void addItems(Item... items) {
        final Session session = sessionFactory.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            Stream.of(items).forEach(session::save);
            tx.commit();
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

}
