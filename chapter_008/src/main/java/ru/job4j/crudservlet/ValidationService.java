package ru.job4j.crudservlet;

import ru.job4j.servlet.Validation;

import java.util.*;

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
        validateInput(List.of(user.getName(), user.getLogin(), user.getRole(), user.getAddress().getCountry(), user.getAddress().getCity()));
        if (validateEmail(user.getLogin())) { // TODO  && validatePassword(user.getPassword())
            return this.store.add(user);
        } else {
            return Optional.empty();
        }
    }

    public boolean update(User user) {
        if (!isIdValid(user.getId())) {
            return false;
        }
        validateInput(List.of(user.getName(), user.getLogin(), user.getRole(), user.getAddress().getCountry(), user.getAddress().getCity()));
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
        return isIdValid(userId) ? this.store.findById(userId) : Optional.empty();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        validateInput(login);
        String validatedLogin = login.strip();
        return validatedLogin.isBlank() ? Optional.empty() : this.store.findByLogin(login);
    }

    @Override
    public Collection<User> findByCountry(String country) {
        validateInput(country);
        String validatedCountry = country.strip();
        return validatedCountry.isBlank() ? Collections.emptyList() : this.store.findByCountry(validatedCountry);
    }

    @Override
    public Collection<User> findByCity(String city) {
        validateInput(city);
        String validatedCity = city.strip();
        return validatedCity.isBlank() ? Collections.emptyList() : this.store.findByCity(validatedCity);
    }

    @Override
    public Optional<Address> getAddress(int id) {
        return isIdValid(id) ? this.store.getAddress(id) : Optional.empty();
    }

    @Override
    public Collection<String> getAllCountries() {
        return this.store.getAllCountries();
    }

    @Override
    public Collection<Address> getAddressesInCountry(String country) {
        validateInput(country);
        String validatedCountry = country.strip();
        return validatedCountry.isBlank() ? Collections.emptyList() : this.store.getAddressesInCountry(validatedCountry);
    }

    public boolean isCredential(String login, String password) {
        List<String> params = List.of(login, password);
        validateInput(params);
        return params.stream().noneMatch(v -> v.strip().isBlank()) && store.isCredential(login, password);
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

