package ru.job4j.crudservlet;

import net.jcip.annotations.ThreadSafe;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * MemoryStore - persistence layout
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@ThreadSafe
public enum MemoryStore implements Store<User> {
    INSTANCE;
    private final AtomicInteger idCounter = new AtomicInteger(1);
    private final ConcurrentHashMap<Integer, User> users;

    MemoryStore() {
        this.users = new ConcurrentHashMap<>();
    }

    @Override
    public Optional<Integer> add(User user) {
        Optional<Integer> userId = Optional.empty();
        if (!users.containsKey(user.getId())
                && !this.users.contains(user)
        ) {
            int id = idCounter.getAndIncrement();
            this.users.put(id, user);
            userId = Optional.of(id);
        }
        return userId;
    }

    @Override
    public boolean update(int userId, User user) {
        return this.users.replace(userId, user) != null;
    }

    @Override
    public void delete(int userId) {
        this.users.remove(userId);
    }

    @Override
    public Collection<User> findAll() {
        return this.users.values();
    }

    @Override
    public Optional<User> findById(int userId) {
        return Optional.of(this.users.get(userId));
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return users.values().stream()
                .filter(user -> login.equals(user.getLogin()))
                .findFirst();
    }
}