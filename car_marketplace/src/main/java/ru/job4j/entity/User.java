package ru.job4j.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "users", schema = "public")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
    private Integer id;

    @NaturalId(mutable = true)
    @Column(name = "phone", unique = true, nullable = false)
    private String phone;

    @Column(name = "name")
    private String name;

    private String salt;

    private String password;

    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registeredAt;

    @OneToMany(mappedBy = "createdBy",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<Item> items = new HashSet<>();

    public User() {
    }

    public static User create(String phone,
                              String name,
                              String salt,
                              String password,
                              LocalDateTime registeredAt
    ) {
        User user = new User();
        user.setPhone(phone);
        user.setName(name);
        user.setSalt(salt);
        user.setPassword(password);
        user.setRegisteredAt(registeredAt.truncatedTo(ChronoUnit.SECONDS));
        return user;
    }

    public Integer getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt.truncatedTo(ChronoUnit.MINUTES);
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    public void addItem(Item item) {
        this.items.add(item);
        item.setCreatedBy(this);
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
        if (!phone.equals(user.phone)) {
            return false;
        }
        if (!name.equals(user.name)) {
            return false;
        }
        return registeredAt.truncatedTo(ChronoUnit.SECONDS).equals(user.registeredAt.truncatedTo(ChronoUnit.SECONDS));
    }

    @Override
    public int hashCode() {
        int result = phone.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + registeredAt.truncatedTo(ChronoUnit.SECONDS).hashCode();
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("phone='" + phone + "'")
                .add("name='" + name + "'")
                .add("registeredAt=" + registeredAt)
                .toString();
    }
}
