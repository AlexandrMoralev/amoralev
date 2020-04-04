package ru.job4j.crudservlet;

import net.jcip.annotations.ThreadSafe;
import ru.job4j.filtersecurity.Role;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        int rootIndex = 0;
        this.users.put(rootIndex,
                User.newBuilder()
                        .setId(rootIndex)
                        .setName("root")
                        .setLogin("root")
                        .setEmail("root@root.ru")
                        .setCreated("at the dawn of a new era")
                        .setPassword("root")
                        .setRole(Role.ROOT)
                        .build()
        );
    }

    private Function<Map.Entry<Integer, User>, User> extractUser() {
        return entry -> User.newBuilder().of(entry.getValue()).setId(entry.getKey()).build();
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
    public boolean update(User user) {
        return this.users.replace(user.getId(), user) != null;
    }

    @Override
    public void delete(int userId) {
        this.users.remove(userId);
    }

    @Override
    public Collection<User> findAll() {
        return this.users.entrySet().stream()
                .map(extractUser())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findById(int userId) {
        User cachedUser = this.users.get(userId);
        return cachedUser == null
                ? Optional.empty()
                : Optional.of(User.newBuilder().of(cachedUser).setId(userId).build());
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return this.users.entrySet().stream()
                .filter(entry -> login.equals(entry.getValue().getLogin()))
                .findFirst()
                .map(extractUser());
    }

    @Override
    public boolean isCredential(String login, String password) {
        return this.users.values().stream().anyMatch(user -> login.equals(user.getLogin()) && password.equals(user.getPassword()));
    }
}