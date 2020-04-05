package ru.job4j.servlet;

import ru.job4j.crudservlet.Store;

import java.util.Collection;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validation interface
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface Validation<E> extends Store<E> {

    Optional<Integer> add(E item);

    boolean update(E updatedItem);

    void delete(int id);

    Collection<E> findAll();

    Optional<E> findById(int id);

    Optional<E> findByLogin(String login);

    Collection<E> findByCountry(String country);

    Collection<E> findByCity(String city);

    boolean isCredential(String login, String password);

    default boolean validateEmail(String email) {
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
    default boolean validatePassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
        Matcher match = pattern.matcher(password);
        return match.matches();
    }

}