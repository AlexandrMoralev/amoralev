package ru.job4j.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * Item
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Entity
@Table(name = "items")
public class Item implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id", unique = true, nullable = false)
    private Integer id;
    private String description;
    private Long created;
    private boolean done;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Item() {
    }

    public Item(Builder builder) {
        this.id = builder.id;
        this.description = builder.description;
        this.created = builder.created;
        this.done = builder.done;
        this.user = builder.user;
    }

    public static Item.Builder of(Item from) {
        Item.Builder item = Item.newBuilder();
        ofNullable(from.getId()).ifPresent(item::setId);
        ofNullable(from.getDescription()).ifPresent(item::setDescription);
        ofNullable(from.getCreated()).ifPresent(item::setCreated);
        Optional.of(from.isDone()).ifPresent(item::setDone);
        return item;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Item)) {
            return false;
        }
        Item item = (Item) o;
        return isDone() == item.isDone()
                && Objects.equals(getDescription(), item.getDescription())
                && Objects.equals(getCreated(), item.getCreated());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescription(), getCreated(), isDone());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Item{");
        sb.append("id=").append(id);
        sb.append(", description='").append(description).append('\'');
        sb.append(", created=").append(created);
        sb.append(", done=").append(done);
        sb.append('}');
        return sb.toString();
    }

    public static Builder newBuilder() {
        return new Item.Builder();
    }

    public static class Builder {
        private Integer id;
        private String description;
        private Long created;
        private boolean done;
        private User user;

        public Builder of(Item item) {
            ofNullable(item.getId()).ifPresent(v -> this.id = v);
            ofNullable(item.getDescription()).ifPresent(v -> this.description = v);
            ofNullable(item.getCreated()).ifPresent(v -> this.created = v);
            Optional.of(item.isDone()).ifPresent(v -> this.done = v);
            Optional.of(item.getUser()).ifPresent(v -> this.user = v);
            return this;
        }

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setCreated(Long created) {
            this.created = created;
            return this;
        }

        public Builder setDone(boolean isDone) {
            this.done = isDone;
            return this;
        }

        public Builder setUser(User user) {
            this.user = user;
            return this;
        }

        public Item build() {
            return new Item(this);
        }

    }
}
