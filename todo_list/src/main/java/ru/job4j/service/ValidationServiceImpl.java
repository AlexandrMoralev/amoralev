package ru.job4j.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.domain.Item;
import ru.job4j.domain.Role;
import ru.job4j.domain.User;
import ru.job4j.exception.ValidationException;
import ru.job4j.persistence.Dao;
import ru.job4j.security.SecurityService;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

/**
 * ValidationServiceImpl
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class ValidationServiceImpl implements ValidationService {

    private static final Logger LOG = LogManager.getLogger(ValidationServiceImpl.class);

    private final SecurityService securityService;
    private final Dao<User> usersDao;
    private final Dao<Item> itemsDao;

    public ValidationServiceImpl(SecurityService securityService,
                                 Dao<User> usersDao,
                                 Dao<Item> itemsDao) {
        this.securityService = securityService;
        this.usersDao = usersDao;
        this.itemsDao = itemsDao;
        LOG.info("ValidationServiceImpl created");
    }

    @Override
    public boolean isCredential(String login, String password) {
        return this.securityService.authenticateUser(login, password);
    }

    @Override
    public Optional<User> getUser(Integer userId) {
        return this.usersDao.findById(userId);
    }

    @Override
    public Optional<User> findUserByLogin(String login) {
        if (login.strip().isBlank()) {
            throw new ValidationException("Invalid user login");
        }
        return this.usersDao.findByCriteria("name", login).stream().findFirst();
    }

    @Override
    public User addUser(String login, String password) {
        if (Stream.of(login, password)
                .map(String::strip)
                .anyMatch(String::isBlank)) {
            throw new ValidationException("Invalid user data");
        }
        User user = new User();
        user.setName(login);
        user.setPassword(password);
        user.setRole(Role.USER);
        user.setTasks(Collections.emptyList());
        return this.securityService.signUp(user);
    }

    @Override
    public Item addItem(Item item, User user) {
        Role role = user.getRole();
        switch (role) {
            case GUEST:
                throw new ValidationException("Not enough permissions for adding items");
            case USER:
                User u = usersDao.findById(user.getId()).orElseThrow(() -> new ValidationException("User not found"));
                Item savedItem = itemsDao.save(Item.of(item).setUser(u).build());
                u.getTasks().add(savedItem);
                usersDao.update(u);
                return savedItem;
            case ADMIN:
                return this.itemsDao.save(item);
            default:
                throw new ValidationException("Unknown user role");
        }
    }

    @Override
    public Optional<Item> getItem(Integer itemId, User user) {
        Role role = user.getRole();
        switch (role) {
            case GUEST:
                throw new ValidationException("Not enough permissions for getting items");
            case USER:
                return usersDao.findById(user.getId())
                        .map(User::getTasks)
                        .orElse(Collections.emptySet())
                        .stream()
                        .filter(i -> itemId.equals(i.getId()))
                        .findFirst();
            case ADMIN:
                return this.itemsDao.findById(itemId);
            default:
                throw new ValidationException("Unknown user role");
        }
    }

    @Override
    public void updateItem(Item item, User user) {
        Role role = user.getRole();
        switch (role) {
            case GUEST:
                throw new ValidationException("Not enough permissions for updating items");
            case USER:
                usersDao.findById(user.getId())
                        .ifPresentOrElse(
                                u -> {
                                    Set<Item> userTasks = u.getTasks();
                                    Set<Item> updatedTasks = userTasks.stream()
                                            .filter(not(i -> item.getId().equals(i.getId())))
                                            .collect(Collectors.toSet());
                                    userTasks.stream()
                                            .filter(i -> item.getId().equals(i.getId()))
                                            .findFirst()
                                            .map(i -> Item.of(i).setDone(true).build()) // TODO its feature, refactor this method if you need to update the task
                                            .ifPresent(i -> {
//                                                itemsDao.update(i);
                                                updatedTasks.add(i);
                                            });
                                    u.setTasks(updatedTasks);
                                    usersDao.update(u);
                                },
                                () -> {
                                    throw new ValidationException("User not found");
                                }
                        );
                break;
            case ADMIN:
                this.itemsDao.update(Item.of(item).setUser(user).build());
                break;
            default:
                throw new ValidationException("Unknown user role");
        }
    }

    @Override
    public void deleteItem(Integer itemId, User user) {
        Role role = user.getRole();
        switch (role) {
            case GUEST:
                throw new ValidationException("Not enough permissions for deleting items");
            case USER:
                usersDao.findById(user.getId())
                        .ifPresentOrElse(
                                u -> {
                                    Set<Item> tasks = u.getTasks().stream()
                                            .filter(not(i -> itemId.equals(i.getId())))
                                            .collect(Collectors.toSet());
                                    u.setTasks(tasks);
                                    usersDao.update(u);
                                },
                                () -> {
                                    throw new ValidationException("User not found");
                                }
                        );
                break;
            case ADMIN:
                this.itemsDao.delete(itemId);
                break;
            default:
                throw new ValidationException("Unknown user role");
        }
    }

    @Override
    public List<Item> getUserTasks(Integer userId) {
        return this.usersDao.findById(userId)
                .map(User::getTasks)
                .orElse(Collections.emptySet())
                .stream()
                .sorted(Comparator.comparing(Item::getCreated).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getAllUsers(User user) {
        Role role = user.getRole();
        switch (role) {
            case ADMIN:
                return this.usersDao.findAll();
            case GUEST:
            case USER:
                throw new ValidationException("Not enough permissions");
            default:
                throw new ValidationException("Unknown user role");
        }
    }

}
