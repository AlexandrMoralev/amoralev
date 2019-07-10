package ru.job4j.servlet;

import ru.job4j.crudservlet.MemoryStore;
import ru.job4j.crudservlet.User;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ValidationService {
    INSTANCE;
    private static final MemoryStore STORE = MemoryStore.INSTANCE;

    ValidationService() {
    }

    public boolean add(String name, String login, String email) {
        validateInput(List.of(name, login, email));
        boolean result = false;
        if (STORE.findByLogin(login).isEmpty()) {
            if (validateEmail(email)) {
                STORE.add(new User(STORE.nextIndex(), name, login, email));
                result = true;
            }
        }
        return result;
    }

    public boolean update(int userId, String name, String login, String email) {
        validateInput(userId);
        validateInput(List.of(name, login, email));
        boolean isUnique = STORE.findById(userId).isPresent()
                && STORE.findByLogin(login).isEmpty()
                && validateEmail(email);
        return isUnique && STORE.update(userId, new User(userId, name, login, email));
    }

    public boolean delete(int userId) {
        validateInput(userId);
        boolean result = false;
        if (STORE.findById(userId).isPresent()) {
            STORE.delete(userId);
            result = true;
        }
        return result;
    }

    public Collection<User> findAll() {
        return STORE.findAll();
    }

    public Optional<User> findById(int userId) {
        validateInput(userId);
        return STORE.findById(userId);
    }

    private boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile("\\A[^@]+@([^@\\.]+\\.)+[^@\\.]+\\z");
        Matcher match = pattern.matcher(email);
        return match.matches();
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

    private <E> void validateInput(List<E> params) {
        if (params.stream().anyMatch(Objects::isNull)) {
            throw new NullPointerException("The reference is null");
        }
    }
}
