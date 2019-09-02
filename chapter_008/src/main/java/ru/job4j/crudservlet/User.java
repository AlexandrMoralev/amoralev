package ru.job4j.crudservlet;

import ru.job4j.filtersecurity.Role;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * User - data model
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class User {
    private final int id;
    private final String name;
    private final String login;
    private final String email;
    private final String created;
    private final String password;
    private final Role role;

    public User(int userId,
                String name,
                String login,
                String email,
                String created,
                String password,
                Role role
    ) {
        this.id = userId;
        this.name = name;
        this.login = login;
        this.email = email;
        this.created = created;
        this.password = password;
        this.role = role;
    }

    public User(int userId,
                String name,
                String login,
                String email,
                String password,
                Role role
    ) {
        this(userId,
                name,
                login,
                email,
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()),
                password,
                role);
    }

    public User(String name,
                String login,
                String email,
                String password,
                Role role
    ) {
        this(-1,
                name,
                login,
                email,
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()),
                password,
                role);
    }

    public User(String name, String login, String email) {
        this(-1,
                name,
                login,
                email,
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()),
                "",
                Role.DEFAULT);
    }


    public int getId() {
        return this.id;
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

    public String getCreated() {
        return this.created;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
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
        return String.format("User{ id=%s, name=%s , login=%s, email=%s, created=%s, role=%s }",
                id, name, login, email, created, role.getDescription());
    }
}
