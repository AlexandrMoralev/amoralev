package ru.job4j.servlet;

import ru.job4j.crudservlet.MemoryStore;
import ru.job4j.crudservlet.Store;
import ru.job4j.crudservlet.User;
import ru.job4j.filtersecurity.Role;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ValidationService {
    INSTANCE;
    private static final Store<User> STORE = MemoryStore.INSTANCE;

    ValidationService() {
    }

    public boolean add(String name, String login, String email, String password, Role role) {
        validateInput(List.of(name, login, email));
        boolean result = false;
        if (STORE.findByLogin(login).isEmpty()) {
            if (validateEmail(email)) {
                STORE.add(new User(name, login, email, password, role));
                result = true;
            }
        }
        return result;
    }

    public boolean update(int userId, String name, String login, String email, String password, Role role) {
        validateInput(userId);
        validateInput(List.of(name, login, email));
        Optional<User> optionalUser = STORE.findById(userId);
        boolean isUnique = optionalUser.map(user -> !user.getLogin().equals(login) || !user.getEmail().equals(email)).orElse(false);
        return isUnique && STORE.update(userId, new User(name, login, email, password, role));
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

    public Optional<User> findByLogin(String login) {
        validateInput(login);
        return login.isBlank() ? Optional.empty() : STORE.findByLogin(login);
    }

    public boolean isCredential(String login, String password) {
        return STORE.isCredential(login, password);
    }

    private boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile("\\A[^@]+@([^@\\.]+\\.)+[^@\\.]+\\z");
        Matcher match = pattern.matcher(email);
        return match.matches();
    }

    /**
     * Validates if the password is secure
     * <p>
     * Explanations:
     * <p>
     * (?=.*[0-9]) a digit must occur at least once
     * (?=.*[a-z]) a lower case letter must occur at least once
     * (?=.*[A-Z]) an upper case letter must occur at least once
     * (?=.*[@#$%^&+=]) a special character must occur at least once
     * (?=\\S+$) no whitespace allowed in the entire string
     * .{8,} at least 8 characters
     *
     * @param password String password to validate
     * @return chec
     */
    private boolean validatePassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
        Matcher match = pattern.matcher(password);
        return match.matches();
    }

    private void validateInput(Object o) {
        if (o == null) {
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
