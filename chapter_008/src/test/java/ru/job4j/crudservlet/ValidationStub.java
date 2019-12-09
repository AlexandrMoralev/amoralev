package ru.job4j.crudservlet;

import ru.job4j.servlet.Validation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum  ValidationStub implements Validation<User> {
    INSTANCE;

    private final Map<Integer, User> store = new HashMap<>();
    private int id = 0;

    ValidationStub() {
    }

    @Override
    public Optional<Integer> add(User user) {
        User storedUser = new User.Builder().of(user).setId(this.id++).build();
        this.store.put(storedUser.getId(), storedUser);
        return Optional.of(storedUser.getId());
    }

    @Override
    public boolean update(User updatedUser) {
        if (this.store.get(updatedUser.getId()) == null) {
            return false;
        }
        return this.store.put(updatedUser.getId(), updatedUser) != null;
    }

    @Override
    public boolean delete(int userId) {
        return Optional.ofNullable(this.store.remove(userId)).isPresent();
    }

    @Override
    public Collection<User> findAll() {
        return this.store.values();
    }

    @Override
    public Optional<User> findById(int userId) {
        return Optional.ofNullable(this.store.get(userId));
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return this.store.values().stream().filter(u -> u.getLogin().equalsIgnoreCase(login)).findFirst();
    }

    @Override
    public boolean isCredential(String login, String password) {
        return this.store.values().stream().anyMatch(u -> u.getLogin().equalsIgnoreCase(login) && u.getPassword().equals(password));
    }
}
