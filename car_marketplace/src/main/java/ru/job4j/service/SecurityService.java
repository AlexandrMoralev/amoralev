package ru.job4j.service;

import ru.job4j.entity.User;

/**
 * SecurityService
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface SecurityService {

    boolean authenticateUser(String phone, String pass);

    User signUp(String phone, String password);

    String generateToken();

}
