package ru.job4j.tracker;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import static java.util.Optional.ofNullable;

/**
 * Item
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version 0.1
 * @since $Id$
 */
public class Item {
    private final Integer id;
    private final String name;
    private final String description;
    private final Long created;
    private final Collection<Comment> comments;

    /**
     * Default instance constructor of the Item class
     *
     * @param name        String name of the Item
     * @param description String short description of the Item
     *                    created long, in ms - date of Item creation
     */
    public Item(final String name, final String description) {
        this.id = null;
        this.name = name;
        this.description = description;
        this.created = System.currentTimeMillis();
        this.comments = Collections.emptyList();
    }

    /**
     * Constructs Item instance by initializing all fields
     *
     * @param id          String item id
     * @param name        String name of the Item
     * @param description String short description of the Item
     * @param created     long, in ms - date of Item creation
     * @param comments    Comment[] comments to the Item
     */
    public Item(final Integer id,
                final String name,
                final String description,
                final Long created,
                final Collection<Comment> comments
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.created = created;
        this.comments = comments;
    }


    private Item(Builder builder) {
        this(builder.id, builder.name, builder.description, builder.created, builder.comments);
    }

    /**
     * @return String id of the Item
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return String name of the Item
     */
    public String getName() {
        return name;
    }

    /**
     * @return String description of the Item
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return long, date of Item creation, in ms
     */
    public Long getCreated() {
        return created;
    }

    /**
     * @return String[] of Item comments
     */
    public Collection<Comment> getComments() {
        return comments;
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
        return Objects.equals(getCreated(), item.getCreated())
                && Objects.equals(getId(), item.getId())
                && Objects.equals(getName(), item.getName())
                && Objects.equals(getDescription(), item.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getCreated());
    }

    @Override
    public String toString() {
        return "Item{"
                + "id='" + id + '\''
                + ", name='" + name + '\''
                + ", description='" + description + '\''
                + ", created=" + created
                + '}';
    }

    public static Builder newBuilder() {
        return new Item.Builder();
    }

    public static class Builder {
        private Integer id;
        private String name;
        private String description;
        private Long created;
        private Collection<Comment> comments;

        public Builder of(Item item) {
            ofNullable(item.getId()).ifPresent(v -> this.id = v);
            ofNullable(item.getName()).ifPresent(v -> this.name = v);
            ofNullable(item.getDescription()).ifPresent(v -> this.description = v);
            ofNullable(item.getCreated()).ifPresent(v -> this.created = v);
            ofNullable(item.getComments()).ifPresent(v -> this.comments = v);
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

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setCreated(Long created) {
            this.created = created;
            return this;
        }

        public Builder setComments(Collection<Comment> comments) {
            this.comments = comments;
            return this;
        }

        public Item build() {
            return new Item(this);
        }
    }

}
