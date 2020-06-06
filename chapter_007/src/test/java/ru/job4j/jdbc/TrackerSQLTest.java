package ru.job4j.jdbc;

import org.junit.jupiter.api.Test;
import ru.job4j.tracker.Comment;
import ru.job4j.tracker.Item;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TrackerSQLTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class TrackerSQLTest {

    private static final Item BASE_ITEM = Item.newBuilder()
            .setName("name")
            .setDescription("desc")
            .setComments(
                    List.of(
                            Comment.newBuilder()
                                    .setComment("comment")
                                    .build()
                    )
            ).build();

    public Connection init() {
        try (InputStream in = TrackerSQL.class.getClassLoader().getResourceAsStream("app.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            return DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void checkConnection() {
        TrackerSQL tracker = new TrackerSQL();
        assertTrue(tracker.init());
    }

    @Test
    public void createItemTest() throws Exception {
        try (TrackerSQL tracker = new TrackerSQL(ConnectionRollback.create(this.init()))) {
            Item item = Item.newBuilder().of(BASE_ITEM).build();

            Integer itemId = tracker.add(item);

            Item itemByName = tracker.findByName("name").get(0);
            Item itemFromAll = tracker.findAll().stream().findFirst().get();
            Item itemById = tracker.findById(itemId).get();
            List<Item> items = List.of(itemByName, itemById, itemFromAll);

            assertTrue(items.stream().allMatch(i -> i.getName().equals("name")));
            assertTrue(items.stream().allMatch(i -> i.getDescription().equals("desc")));
            assertTrue(items.stream().map(i -> i.getComments().stream().findFirst().get().getComment()).allMatch(c -> c.equals("comment")));
        }
    }

    @Test
    public void updateItemTest() throws Exception {
        try (TrackerSQL tracker = new TrackerSQL(ConnectionRollback.create(this.init()))) {
            Item item = Item.newBuilder().of(BASE_ITEM).build();

            Integer itemId = tracker.add(item);
            Item storedItem = tracker.findById(itemId).get();

            Item updatedItem = Item.newBuilder()
                    .of(storedItem)
                    .setName("updated name")
                    .setDescription("updated desc")
                    .setComments(
                            List.of(Comment.newBuilder()
                                    .setComment("updated comment")
                                    .build()
                            )
                    ).build();

            tracker.update(updatedItem);

            assertEquals(1, tracker.findAll().size());
            Item itemFromAll = tracker.findAll().stream().findFirst().get();
            Item itemByName = tracker.findByName("updated name").get(0);
            Item itemById = tracker.findById(itemId).get();
            List<Item> items = List.of(itemByName, itemById, itemFromAll);

            assertTrue(items.stream().allMatch(i -> i.getName().equals("updated name")));
            assertTrue(items.stream().allMatch(i -> i.getDescription().equals("updated desc")));
            assertTrue(items.stream().map(i -> i.getComments().stream().findFirst().get().getComment()).allMatch(c -> c.equals("comment")));
        }
    }

    @Test
    public void deleteItemTest() throws Exception {
        try (TrackerSQL tracker = new TrackerSQL(ConnectionRollback.create(this.init()))) {
            Item removableItem = Item.newBuilder().of(BASE_ITEM).build();
            Item savedItem = Item.newBuilder()
                    .setName("saved name")
                    .setDescription("saved description")
                    .setComments(
                            List.of(
                            Comment.newBuilder()
                                    .setComment("saved commentary")
                                    .build())
                    ).build();

            Integer removableItemId = tracker.add(removableItem);
            Integer savedItemId = tracker.add(savedItem);

            assertEquals(2, tracker.findAll().size());

            tracker.delete(removableItemId);

            List<Item> items = tracker.findAll();
            assertEquals(1, items.size());
            assertEquals("saved name", items.get(0).getName());

            assertTrue(tracker.findByName("name").isEmpty());
            assertTrue(tracker.findById(removableItemId).isEmpty());

            assertFalse(tracker.findByName("saved name").isEmpty());
            assertFalse(tracker.findById(savedItemId).isEmpty());
        }
    }
}
