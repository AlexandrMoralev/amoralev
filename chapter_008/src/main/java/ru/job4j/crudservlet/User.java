package ru.job4j.crudservlet;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * User - data model
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class User implements Comparable<User> {
    private static final AtomicLong ID_COUNTER = new AtomicLong(0L);
    private long id;
    private final String name;
    private final String login;
    private final String email;
    private final LocalDateTime created;

    public User(String name, String login, String email) {
        this.id = ID_COUNTER.getAndIncrement();
        this.name = name;
        this.login = login;
        this.email = email;
        this.created = LocalDateTime.now();
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
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
        return getId() == user.getId()
                && Objects.equals(getName(), user.getName())
                && Objects.equals(getLogin(), user.getLogin())
                && Objects.equals(getEmail(), user.getEmail())
                && Objects.equals(getCreated(), user.getCreated()
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getLogin(), getEmail(), getCreated());
    }

    @Override
    public String toString() {
        return String.format("User{ id=%s, name=%s , login=%s, email=%s, created=%s }",
                id, name, login, email, created);
    }

    @Override
    public int compareTo(User o) {
        return Long.compare(this.id, o.getId());
    }
}
