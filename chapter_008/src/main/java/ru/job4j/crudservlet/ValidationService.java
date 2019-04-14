package ru.job4j.crudservlet;

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
    private final Store store = MemoryStore.INSTANCE;

    ValidationService() {
    }

    public boolean add(User user) {
        validateInput(user);
        validateInput(user.getId());
        return !this.store.findAll().contains(user) && this.store.add(user);
    }

    public boolean update(long userId, User diffUser) {
        validateInput(userId);
        validateInput(diffUser);

        Optional<User> current = this.store.findById(userId);
        User updated = null;
        if (current.isPresent() && !current.get().equals(diffUser)) {
            updated = combine(current.get(), diffUser);
            this.store.update(updated);
        }
        return updated != null;
    }

    private User combine(User current, User diffUser) {
        long userId = current.getId();
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
        User updated = new User(name, login, email);
        updated.setId(userId);
        return updated;
    }

    public boolean delete(long userId) {
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

    public Optional<User> findById(long userId) {
        validateInput(userId);
        return this.store.findById(userId);
    }

    private void validateInput(User user) {
        if (user == null) {
            throw new NullPointerException("The reference is null");
        }
    }

    private void validateInput(long userId) {
        if (userId < 0) {
            throw new IllegalStateException("userId is invalid " + userId);
        }
    }
}

