package ru.job4j.service.impl;

import ru.job4j.entity.User;
import ru.job4j.exception.ValidationException;
import ru.job4j.persistence.impl.UsersDao;
import ru.job4j.service.AuthService;
import ru.job4j.service.SecurityService;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * AuthServiceImpl
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class AuthServiceImpl implements AuthService {

    private final SecurityService securityService;
    private final UsersDao usersDao;

    public AuthServiceImpl(SecurityService securityService,
                           UsersDao usersDao
    ) {
        this.securityService = securityService;
        this.usersDao = usersDao;
    }

    @Override
    public Optional<User> findUserByPhone(String phone) {
        return this.usersDao.findByPhone(phone);
    }

    @Override
    public boolean isCredential(String phone, String password) {
        return this.securityService.authenticateUser(phone, password);
    }

    @Override
    public User addUser(String phone, String password) {
        if (Stream.of(phone, password)
                .map(String::strip)
                .anyMatch(String::isBlank)) {
            throw new ValidationException("Invalid user data");
        }
        return this.securityService.signUp(phone, password);
    }

    @Override
    public Optional<User> getUser(Integer userId) {
        return this.usersDao.find(userId);
    }

    @Override
    public Collection<User> getAllUsers() {
        return this.usersDao.findAll().collect(Collectors.toList());
    }

}
