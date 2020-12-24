package ru.job4j.service;

import ru.job4j.entity.User;

import java.util.Collection;
import java.util.Optional;

/**
 * AuthService
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface AuthService {

    Optional<User> findUserByPhone(String phone);

    boolean isCredential(String phone, String password);

    User addUser(String phone, String password);

    Optional<User> getUser(Integer userId);

    Collection<User> getAllUsers();
}
