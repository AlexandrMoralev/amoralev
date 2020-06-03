package ru.job4j.crudservlet;

import ru.job4j.filtersecurity.Role;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * User - data model
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class User {
    private final Integer id;
    private final String name;
    private final String login; // == email
    private final String created;
    private final String password;
    private final Role role;
    private final Address address;
    private final String photoId;

    private User(Integer userId,
                 String name,
                 String login,
                 String created,
                 String password,
                 Role role,
                 Address address,
                 String photoId
    ) {
        this.id = userId;
        this.name = name;
        this.login = login;
        this.created = created;
        this.password = password;
        this.role = role;
        this.address = address;
        this.photoId = photoId;
    }

    private User(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.login = builder.login;
        this.created = builder.created;
        this.password = builder.password;
        this.role = builder.role;
        this.address = builder.address;
        this.photoId = builder.photoId;
    }

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getLogin() {
        return this.login;
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

    public Address getAddress() {
        return address;
    }

    public String getPhotoId() {
        return photoId;
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
        return Objects.equals(getLogin(), user.getLogin());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLogin());
    }

    @Override
    public String toString() {
        return String.format("User{ id=%s, name=%s , login=%s, created=%s, role=%s, address=%s, hasPhoto=%s }",
                id, name, login, created, role.getDescription(), address.toString(), photoId.isBlank());
    }

    public static Builder newBuilder() {
        return new User.Builder();
    }

    public static class Builder {
        private Integer id;
        private String name;
        private String login;
        private String created = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now());
        private String password;
        private Role role;
        private Address address;
        private String photoId;

        public Builder of(User user) {
            ofNullable(user.getId()).ifPresent(v -> this.id = v);
            ofNullable(user.getName()).ifPresent(v -> this.name = v);
            ofNullable(user.getLogin()).ifPresent(v -> this.login = v);
            ofNullable(user.getCreated()).ifPresent(v -> this.created = v);
            ofNullable(user.getPassword()).ifPresent(v -> this.password = v);
            Optional.of(user.getRole()).ifPresent(v -> this.role = v);
            Optional.of(user.getAddress()).ifPresent(v -> this.address = v);
            Optional.ofNullable(user.getPhotoId()).ifPresent(v -> this.photoId = v);
            return this;
        }

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setLogin(String login) {
            this.login = login;
            return this;
        }

        public Builder setCreated(String created) {
            this.created = created;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setRole(Role role) {
            this.role = role;
            return this;
        }

        public Builder setAddress(Address address) {
            this.address = address;
            return this;
        }

        public Builder setPhotoId(String photoId) {
            this.photoId = photoId;
            return this;
        }

        public User build() {
            return new User(this);
        }

    }
}
