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
    private final Store<User> store = MemoryStore.INSTANCE;

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
        User.Builder builder = new User.Builder().of(current);
        getNonEmptyString(diffUser.getName()).ifPresent(builder::setName);
        getNonEmptyString(diffUser.getLogin()).ifPresent(builder::setLogin);
        getNonEmptyString(diffUser.getEmail()).ifPresent(builder::setEmail);
        getNonEmptyString(diffUser.getPassword()).ifPresent(builder::setPassword);
        Optional.ofNullable(diffUser.getRole()).ifPresent(builder::setRole);
        return builder.build();
    }

    private static Optional<String> getNonEmptyString(String value) {
        return Optional.ofNullable(value).filter(s -> !s.isBlank());
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

