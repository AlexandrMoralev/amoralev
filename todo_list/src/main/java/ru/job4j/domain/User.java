package ru.job4j.domain;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static java.util.Optional.ofNullable;

/**
 * User
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
    private Integer id;

    @NaturalId
    private String name;

    private String salt;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.REMOVE},
            orphanRemoval = true)
    private Set<Item> tasks = new HashSet<>();

    public User() {
    }

    private User(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.salt = builder.salt;
        this.password = builder.password;
        this.role = builder.role;
        this.tasks = builder.tasks;
    }

    public static User of(String name, Role role) {
        User user = new User();
        user.name = name;
        user.role = role;
        return user;
    }

    public static User.Builder of(User from) {
        User.Builder user = User.newBuilder();
        ofNullable(from.getId()).ifPresent(user::setId);
        ofNullable(from.getName()).ifPresent(user::setName);
        ofNullable(from.getSalt()).ifPresent(user::setSalt);
        ofNullable(from.getPassword()).ifPresent(user::setPassword);
        ofNullable(from.getRole()).ifPresent(user::setRole);
        ofNullable(from.getTasks()).ifPresent(user::setTasks);
        return user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Item> getTasks() {
        return tasks;
    }

    public void setTasks(Collection<Item> tasks) {
        this.tasks = new HashSet<>(tasks);
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
        return Objects.equals(this.getName(), user.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", role=").append(role);
        sb.append('}');
        return sb.toString();
    }

    public static Builder newBuilder() {
        return new User.Builder();
    }

    public static class Builder {
        private Integer id;
        private String name;
        private Role role;
        private String salt;
        private String password;
        private Set<Item> tasks = new HashSet<>();

        public Builder of(User user) {
            ofNullable(user.getId()).ifPresent(v -> this.id = v);
            ofNullable(user.getName()).ifPresent(v -> this.name = v);
            ofNullable(user.getRole()).ifPresent(v -> this.role = v);
            ofNullable(user.getSalt()).ifPresent(v -> this.salt = v);
            ofNullable(user.getPassword()).ifPresent(v -> this.password = v);
            ofNullable(user.getTasks()).ifPresent(v -> this.tasks = v);
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

        public Builder setRole(Role role) {
            this.role = role;
            return this;
        }

        public Builder setSalt(String salt) {
            this.salt = salt;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }


        public Builder setTasks(Set<Item> tasks) {
            this.tasks = tasks;
            return this;
        }

        public User build() {
            return new User(this);
        }

    }
}
