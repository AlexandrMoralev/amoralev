package ru.job4j.service;

import ru.job4j.domain.Item;
import ru.job4j.domain.User;

import java.util.List;
import java.util.Optional;

/**
 * ValidationService
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface ValidationService {

    boolean isCredential(String login, String password);

    Optional<User> getUser(Integer userId);

    Optional<User> findUserByLogin(String login);

    User addUser(String login, String password);

    Item addItem(Item item, User user);

    Optional<Item> getItem(Integer itemId, User user);

    void updateItem(Item item, User user);

    void deleteItem(Integer itemId, User user);

    List<Item> getUserTasks(Integer userId);

    List<User> getAllUsers(User user);
}
