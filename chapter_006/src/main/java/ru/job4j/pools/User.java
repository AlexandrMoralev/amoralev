package ru.job4j.pools;

import java.util.Objects;

/**
 * User
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class User {
    private final String username;
    private String email;

    /**
     * Constructs User instance
     * @param username notnull String, name of this User
     * @param email notnull String, User's email
     */
    public User(final String username, final String email) {
        this.username = username == null ? "" : username;
        this.email = email == null ? "" : email;
    }

    public String getName() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(username, user.username)
                && Objects.equals(getEmail(), user.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, getEmail());
    }

    @Override
    public String toString() {
        return "User{"
                + "username='" + username + '\''
                + ", email='" + email + '\''
                + '}';
    }
}
