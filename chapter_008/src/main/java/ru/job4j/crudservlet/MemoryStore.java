package ru.job4j.crudservlet;

import net.jcip.annotations.ThreadSafe;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

//import ru.job4j.crudservlet.Store;

/**
 * MemoryStore - persistence layout
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@ThreadSafe
public enum MemoryStore implements Store {
    INSTANCE;
    private final AtomicInteger idCounter = new AtomicInteger(1);
    private final ConcurrentHashMap<Integer, User> users;

    MemoryStore() {
        this.users = new ConcurrentHashMap<>();
    }

    @Override
    public boolean add(User user) {
        boolean result = !this.users.contains(user);
        if (result) {
            user.setId(idCounter.getAndIncrement());
            this.users.put(user.getId(), user);
        }
        return result;
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