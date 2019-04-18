package ru.job4j.crudservlet;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * User - data model
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class User {
    private int id;
    private final String name;
    private final String login;
    private final String email;
    private final LocalDateTime created;

    public User(String name, String login, String email) {
        this.id = -1;
        this.name = name;
        this.login = login;
        this.email = email;
        this.created = LocalDateTime.now();
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public String getLogin() {
        return this.login;
    }

    public String getEmail() {
        return this.email;
    }

    public LocalDateTime getCreated() {
        return this.created;
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
        return Objects.equals(getLogin(), user.getLogin())
                && Objects.equals(getEmail(), user.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLogin(), getEmail());
    }

    @Override
    public String toString() {
        return String.format("User{ id=%s, name=%s , login=%s, email=%s, created=%s }",
                id, name, login, email, created);
    }
}
