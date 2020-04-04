package ru.job4j.crudservlet;

import ru.job4j.servlet.Validation;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        validateInput(List.of(user.getName(), user.getLogin(), user.getEmail()));
        if (validateEmail(user.getEmail())) { // TODO  && validatePassword(user.getPassword())
            return this.store.add(user);
        } else {
            return Optional.empty();
        }
    }

    public boolean update(User user) {
        if (!isIdValid(user.getId())) {
            return false;
        }
        validateInput(List.of(user.getName(), user.getLogin(), user.getEmail()));
        return this.store.update(user);
    }

    public void delete(int userId) {
        if (isIdValid(userId)) {
            this.store.delete(userId);
        }
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
        String validatedLogin = login.strip();
        if (validatedLogin.isBlank()) {
            return Optional.empty();
        } else {
            return this.store.findByLogin(login);
        }
    }

    @Override
    public Collection<User> findByCountry(String country) {
        validateInput(country);
        String validatedCountry = country.strip();
        if (validatedCountry.isBlank()) {
            return Collections.emptyList();
        } else {
            return this.store.findByCountry(validatedCountry);
        }
    }

    @Override
    public Collection<User> findByCity(String city) {
        validateInput(city);
        String validatedCity = city.strip();
        if (validatedCity.isBlank()) {
            return Collections.emptyList();
        } else {
            return this.store.findByCity(validatedCity);
        }
    }

    public boolean isCredential(String login, String password) {
        List<String> params = List.of(login, password);
        validateInput(params);
        return params.stream().noneMatch(v -> v.strip().isBlank()) && store.isCredential(login, password);
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

    private void validateInput(Object obj) {
        if (obj == null) {
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

