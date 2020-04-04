package ru.job4j.servlet;

import ru.job4j.crudservlet.MemoryStore;
import ru.job4j.crudservlet.Store;
import ru.job4j.crudservlet.User;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ValidationService {
    INSTANCE;
    private static final Store<User> STORE = MemoryStore.INSTANCE;

    ValidationService() {
    }

    public Optional<Integer> add(User user) {
        validateInput(List.of(user.getName(), user.getLogin(), user.getEmail()));
        if (STORE.findByLogin(user.getLogin()).isEmpty()) {
            if (validateEmail(user.getEmail())) {
                return STORE.add(user);
            }
        }
        return Optional.empty();
    }

    public boolean update(User user) {
        if (!isIdValid(user.getId())) {
            return false;
        }
        validateInput(List.of(user.getName(), user.getLogin(), user.getEmail()));
        boolean isUnique = STORE.findById(user.getId())
                .map(isUserUnique(user))
                .orElse(false);
        return isUnique && STORE.update(user);
    }

    private Function<User, Boolean> isUserUnique(User user) {
        return u -> !u.getLogin().equals(user.getLogin()) || !u.getEmail().equals(user.getEmail());
    }

    public void delete(int userId) {
        if (isIdValid(userId)) {
            STORE.delete(userId);
        }
    }

    public Collection<User> findAll() {
        return STORE.findAll();
    }

    public Optional<User> findById(int userId) {
        if (!isIdValid(userId)) {
            return Optional.empty();
        }
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

    private boolean isIdValid(Integer userId) {
        return userId != null && userId > 0;
    }

    private <E> void validateInput(List<E> params) {
        if (params.stream().anyMatch(Objects::isNull)) {
            throw new NullPointerException("The reference is null");
        }
    }
}
