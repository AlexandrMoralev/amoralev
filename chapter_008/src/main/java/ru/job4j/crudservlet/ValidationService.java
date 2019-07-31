package ru.job4j.crudservlet;

import ru.job4j.servlet.DBStore;

import java.util.Collection;
import java.util.Optional;

/**
 * ValidationService - logic layout
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public enum ValidationService {
    INSTANCE;
    private final Store<User> store = DBStore.INSTANCE;

    ValidationService() {
    }

    public Optional<Integer> add(User user) {
        validateInput(user);
        return this.store.add(user);
    }

    public boolean update(int userId, User diffUser) {
        validateInput(userId);
        validateInput(diffUser);

        Optional<User> current = this.store.findById(userId);
        User updated = null;
        if (current.isPresent() && !current.get().equals(diffUser)) {
            updated = combine(current.get(), diffUser);
            this.store.update(userId, updated);
        }
        return updated != null;
    }

    private User combine(User current, User diffUser) {
        int userId = current.getId();
        String name = diffUser.getName() == null
                || diffUser.getName().isBlank()
                ? current.getName()
                : diffUser.getName();
        String login = diffUser.getLogin() == null
                || diffUser.getLogin().isBlank()
                ? current.getLogin()
                : diffUser.getLogin();
        String email = diffUser.getEmail() == null
                || diffUser.getEmail().isBlank()
                ? current.getEmail()
                : diffUser.getEmail();
        return new User(userId, name, login, email);
    }

    public boolean delete(int userId) {
        validateInput(userId);
        boolean result = this.store.findById(userId).isPresent();
        if (result) {
            this.store.delete(userId);
        }
        return result;
    }

    public Collection<User> findAll() {
        return this.store.findAll();
    }

    public Optional<User> findById(int userId) {
        validateInput(userId);
        return this.store.findById(userId);
    }

    private void validateInput(User user) {
        if (user == null) {
            throw new NullPointerException("The reference is null");
        }
    }

    private void validateInput(int userId) {
        if (userId < 0) {
            throw new IllegalStateException("userId is invalid " + userId);
        }
    }
}

