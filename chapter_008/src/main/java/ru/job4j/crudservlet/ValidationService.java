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

    public boolean update(long userId, User user) {
        validateInput(userId);
        validateInput(user);
        return !this.store.findAll().contains(user) && this.store.update(userId, user);
    }

    public void delete(long userId) {
        validateInput(userId);
        this.store.delete(userId);
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

