package ru.job4j.security;

import ru.job4j.domain.User;

/**
 * SecurityService
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface SecurityService {

    boolean authenticateUser(String login, String pass);

    User signUp(User newUser);

    String generateToken();

}
