package ru.job4j.crudservlet;

import net.jcip.annotations.ThreadSafe;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListSet;

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
    private final ConcurrentSkipListSet<User> users;

    MemoryStore() {
        this.users = new ConcurrentSkipListSet<>();
    }

    @Override
    public boolean add(User user) {
        return users.add(user);
    }

    @Override
    public boolean update(User user) {
        this.users.removeIf(aUser -> user.getId() == aUser.getId());
        return users.add(user);
    }

    @Override
    public void delete(long userId) {
        this.users.stream()
                .filter(user -> userId == user.getId())
                .findFirst()
                .ifPresent(this.users::remove);
    }

    @Override
    public Collection<User> findAll() {
        return this.users;
    }

    @Override
    public Optional<User> findById(long userId) {
        return this.users.stream()
                .filter(user -> userId == user.getId())
                .findFirst();
    }
}
