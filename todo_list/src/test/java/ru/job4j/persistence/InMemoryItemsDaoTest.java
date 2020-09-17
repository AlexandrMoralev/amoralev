package ru.job4j.persistence;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.gen5.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.TestAppContext;
import ru.job4j.domain.Item;
import ru.job4j.domain.Role;
import ru.job4j.domain.User;
import ru.job4j.util.HibernateUtils;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
public class InMemoryItemsDaoTest {

    private final Dao<Item> itemsDao;
    private final Dao<User> usersDao;
    private final HibernateUtils daoUtils;

    private Item firstItem;
    private Item secondItem;
    private Item thirdItem;

    public InMemoryItemsDaoTest() {
        this.itemsDao = TestAppContext.INSTANCE.itemsDao;
        this.usersDao = TestAppContext.INSTANCE.usersDao;
        this.daoUtils = TestAppContext.INSTANCE.daoUtils;
    }

    @BeforeEach
    private void init() {
        firstItem = Item.newBuilder().setId(1).setDescription("first description").setCreated(System.currentTimeMillis()).setDone(false).build();
        secondItem = Item.newBuilder().setId(2).setDescription("second description").setCreated(System.currentTimeMillis() + 1L).setDone(true).build();
        thirdItem = Item.newBuilder().setId(null).setDescription("third description").setCreated(1L).setDone(false).build();
    }

    @Transactional
    @Test
    public void addUserTest() {
        User user = User.newBuilder()
                .setName("add user test")
                .setRole(Role.USER)
                .setPassword("qwerty")
                .setSalt("salt")
                .build();
        user = usersDao.save(user);

        assertEquals("add user test", user.getName());
        assertEquals(Role.USER, user.getRole());
        assertEquals("salt", user.getSalt());
        assertEquals("qwerty", user.getPassword());
        assertTrue(user.getTasks().isEmpty());

        assertEquals(user, usersDao.findById(user.getId()).orElseThrow(RuntimeException::new));
        assertTrue(usersDao.findAll().stream().allMatch(user::equals));
        assertTrue(usersDao.findByCriteria("name", user.getName()).stream().allMatch(user::equals));
        assertTrue(usersDao.findByCriteria("name", "user").isEmpty());
    }

    @Transactional
    @Test
    public void updateUserTest() {
        User user = User.newBuilder()
                .setName("update user test")
                .setRole(Role.USER)
                .setPassword("qwerty")
                .setSalt("salt")
                .build();
        user = usersDao.save(user);

        User updatedUser = User.of(user)
                .setRole(Role.GUEST)
                .setPassword("updated qwerty")
                .setSalt("updated salt")
                .build();

        usersDao.update(updatedUser);

        User guest = usersDao.findById(updatedUser.getId()).orElseThrow(RuntimeException::new);

        assertEquals("update user test", guest.getName());
        assertEquals(Role.GUEST, guest.getRole());
        assertEquals("updated salt", guest.getSalt());
        assertEquals("updated qwerty", guest.getPassword());
        assertTrue(guest.getTasks().isEmpty());

        User userWithTasks = User.of(guest)
                .setRole(Role.USER)
                .build();

        Set<Item> tasks = Set.of(firstItem, secondItem, thirdItem).stream()
                .map(item -> Item.of(item).setUser(userWithTasks).build())
                .map(itemsDao::save)
                .collect(Collectors.toSet());

        userWithTasks.setTasks(tasks);
        usersDao.update(userWithTasks);

        User savedUserWithTasks = usersDao.findById(userWithTasks.getId()).orElseThrow(RuntimeException::new);

        assertEquals("update user test", savedUserWithTasks.getName());
        assertEquals(Role.USER, savedUserWithTasks.getRole());
        assertEquals("updated salt", savedUserWithTasks.getSalt());
        assertEquals("updated qwerty", savedUserWithTasks.getPassword());
        assertIterableEquals(tasks, savedUserWithTasks.getTasks());

        assertTrue(usersDao.findAll().stream().allMatch(savedUserWithTasks::equals));
        assertTrue(usersDao.findByCriteria("name", user.getName()).stream().allMatch(user::equals));
        assertTrue(usersDao.findByCriteria("name", "qwerty").isEmpty());
    }

    @Transactional
    @Test
    public void deleteUserTest() {
        User user1 = User.newBuilder()
                .setName("delete user test")
                .setRole(Role.USER)
                .setPassword("qwerty")
                .setSalt("salt")
                .build();

        User user2 = User.newBuilder()
                .setName("deletable user")
                .setRole(Role.USER)
                .setPassword("qwerty")
                .setSalt("salt")
                .build();

        user1 = usersDao.save(user1);
        user2 = usersDao.save(user2);

        assertIterableEquals(List.of(user1, user2), usersDao.findAll());

        usersDao.delete(user2.getId());

        user1 = usersDao.findById(user1.getId()).orElseThrow(RuntimeException::new);
        assertEquals("delete user test", user1.getName());
        assertEquals(Role.USER, user1.getRole());
        assertEquals("salt", user1.getSalt());
        assertEquals("qwerty", user1.getPassword());
        assertTrue(user1.getTasks().isEmpty());

        assertTrue(usersDao.findById(user2.getId()).isEmpty());
        assertTrue(usersDao.findAll().stream().noneMatch(user2::equals));

        assertTrue(usersDao.findByCriteria("name", user1.getName()).stream().allMatch(user1::equals));
        assertTrue(usersDao.findByCriteria("name", user2.getName()).isEmpty());
    }

    @Transactional
    @Test
    public void addItemTest() {
        User user = User.newBuilder()
                .setName("update user test")
                .setRole(Role.USER)
                .setPassword("qwerty")
                .setSalt("salt")
                .build();

        user = usersDao.save(user);

        User userWithTasks = User.of(user).build();
        Set<Item> tasks = Set.of(firstItem, secondItem, thirdItem).stream()
                .map(item -> Item.of(item).setUser(userWithTasks).build())
                .map(itemsDao::save)
                .collect(Collectors.toSet());
        userWithTasks.setTasks(tasks);

        usersDao.update(userWithTasks);

        user = usersDao.findById(userWithTasks.getId()).orElseThrow(RuntimeException::new);

        assertEquals(user.getName(), userWithTasks.getName());
        assertEquals(user.getRole(), userWithTasks.getRole());
        assertEquals(user.getSalt(), userWithTasks.getSalt());
        assertEquals(user.getPassword(), userWithTasks.getPassword());
        assertIterableEquals(user.getTasks(), userWithTasks.getTasks());

        Set<Item> userTasks = user.getTasks().stream()
                .map(Item::getId)
                .map(itemsDao::findById)
                .flatMap(Optional::stream)
                .collect(Collectors.toSet());

        assertTrue(userTasks.containsAll(itemsDao.findAll()));
        assertTrue(userTasks.containsAll(itemsDao.findByCriteria("description", "description")));

        List<Item> itemsByCriteria = itemsDao.findByCriteria("description", "first");
        assertEquals(1, itemsByCriteria.size());
        assertEquals(firstItem, itemsByCriteria.get(0));

        assertTrue(itemsDao.findByCriteria("description", "fourth").isEmpty());

    }

    @Transactional
    @Test
    public void updateItemTest() {
        User user = User.newBuilder()
                .setName("update user test")
                .setRole(Role.USER)
                .setPassword("qwerty")
                .setSalt("salt")
                .build();

        user = usersDao.save(user);

        User userWithTasks = User.of(user).build();
        Set<Item> tasks = Set.of(firstItem, secondItem, thirdItem).stream()
                .map(item -> Item.of(item).setUser(userWithTasks).build())
                .map(itemsDao::save)
                .collect(Collectors.toSet());
        userWithTasks.setTasks(tasks);
        usersDao.update(userWithTasks);

        user = usersDao.findById(userWithTasks.getId()).orElseThrow(RuntimeException::new);

        assertEquals(user.getName(), userWithTasks.getName());
        assertEquals(user.getRole(), userWithTasks.getRole());
        assertEquals(user.getSalt(), userWithTasks.getSalt());
        assertEquals(user.getPassword(), userWithTasks.getPassword());
        assertIterableEquals(user.getTasks(), userWithTasks.getTasks());

        Set<Item> updatedTasks = user.getTasks();
        updatedTasks.removeIf(Item::isDone);
        updatedTasks = updatedTasks.stream().map(item -> {
            if (item.getDescription().startsWith("first")) {
                return Item.of(item).setCreated(System.currentTimeMillis()).build();
            } else {
                return item;
            }
        }).collect(Collectors.toSet());
        user.setTasks(updatedTasks);
        usersDao.update(user);

        Set<Item> userTasks = usersDao.findById(user.getId())
                .map(User::getTasks)
                .orElseThrow(RuntimeException::new);

        assertTrue(userTasks.containsAll(itemsDao.findAll()));
        assertTrue(userTasks.containsAll(itemsDao.findByCriteria("description", "description")));

        List<Item> itemsByCriteria = itemsDao.findByCriteria("description", "first");
        assertEquals(1, itemsByCriteria.size());
        assertEquals(firstItem, itemsByCriteria.get(0));

        assertTrue(itemsDao.findByCriteria("description", "fourth").isEmpty());
    }

    @Transactional
    @Test
    public void deleteItemTest() {
        Item first = itemsDao.save(firstItem);
        Item second = itemsDao.save(secondItem);
        Item third = itemsDao.save(thirdItem);
        itemsDao.delete(first.getId());
        itemsDao.delete(second.getId());
        Collection<Item> storedItems = itemsDao.findAll();
        assertEquals(1, storedItems.size());
        assertTrue(storedItems.stream().allMatch(third::equals));
    }

    @AfterEach
    public void cleanUp() {
        final Session session = daoUtils.getSessionFactory().openSession();
        final Transaction tx = session.beginTransaction();
        try {
            usersDao.findAll().stream().map(User::getId).forEach(usersDao::delete);
            itemsDao.findAll().stream().map(Item::getId).forEach(itemsDao::delete);
            session.clear();
            tx.commit();
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @AfterAll
    public void close() {
        daoUtils.shutdown();
    }

}
