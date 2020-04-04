package ru.job4j.crudservlet;

import ru.job4j.servlet.Validation;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public enum ValidationStub implements Validation<User> {
    INSTANCE;

    private Map<Integer, User> store;
    private int id = 0;

    ValidationStub() {
    }

    ValidationStub(Map<Integer, User> store) {
        this.store = store;
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
    public void delete(int userId) {
        this.store.remove(userId);
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
    public Collection<User> findByCountry(String country) {
        return this.store.values().stream().filter(u -> u.getCountry().equalsIgnoreCase(country)).collect(Collectors.toList());
    }

    @Override
    public Collection<User> findByCity(String city) {
        return this.store.values().stream().filter(u -> u.getCity().equalsIgnoreCase(city)).collect(Collectors.toList());
    }

    @Override
    public boolean isCredential(String login, String password) {
        return this.store.values().stream().anyMatch(u -> u.getLogin().equalsIgnoreCase(login) && u.getPassword().equals(password));
    }
}
