package ru.job4j.crudservlet;

import ru.job4j.servlet.Validation;

import java.util.Collection;
import java.util.Optional;

/**
 * ValidationService - logic layout
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public enum ValidationService implements Validation<User> {
    INSTANCE;
    private final Store<User> store = MemoryStore.INSTANCE;

    ValidationService() {
    }

    public Optional<Integer> add(User user) {
        validateInput(user);
        return this.store.add(user);
    }

    public boolean update(User diffUser) {
        validateInput(diffUser);
        Optional<User> current = this.store.findById(diffUser.getId());
        return current.filter(user -> !user.equals(diffUser))
                .map(user -> {
                    User updated = combine(user, diffUser);
                    this.store.update(updated);
                    return updated;
                }).isPresent();
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
        if (!isIdValid(userId)) {
            return false;
        }
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
        if (!isIdValid(userId)) {
            return Optional.empty();
        }
        return this.store.findById(userId);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        validateInput(login);
        return this.store.findByLogin(login);
    }

    public boolean isCredential(String login, String password) {
        return store.isCredential(login, password);
    }

    private void validateInput(Object obj) {
        if (obj == null) {
            throw new NullPointerException("The reference is null");
        }
    }

    private boolean isIdValid(Integer userId) {
        return userId != null && userId > 0;
    }
}

