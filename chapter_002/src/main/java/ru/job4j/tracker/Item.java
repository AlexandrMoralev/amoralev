package ru.job4j.tracker;

import java.util.Objects;

/**
 * Item
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version 0.1
 * @since $Id$
 */
public class Item {
    private static final int INIT_COMMENTS_SIZE = 16;
    private String id;
    private String name;
    private String description;
    private long created;
    private String[] comments;

    /**
     * Default instance constructor of the Item class
     *
     * @param name        String name of the Item
     * @param description String short description of the Item
     *                    created long, in ms - date of Item creation
     */
    public Item(final String name, final String description) {
        this.name = name;
        this.description = description;
        this.created = System.currentTimeMillis();
        this.comments = new String[INIT_COMMENTS_SIZE];
    }

    /**
     * Constructs Item instance by initializing all fields
     *
     * @param id          String item id
     * @param name        String name of the Item
     * @param description String short description of the Item
     * @param created     long, in ms - date of Item creation
     * @param comments    String[] comments to the Item
     */
    public Item(final String id,
                final String name,
                final String description,
                final long created,
                final String[] comments
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.created = created;
        this.comments = comments;
    }

    /**
     * @return String id of the Item
     */
    public String getId() {
        return id;
    }

    /**
     * @param id String id of the Item
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return String name of the Item
     */
    public String getName() {
        return name;
    }

    /**
     * @param name String name of the Item
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return String description of the Item
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description String
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return long, date of Item creation, in ms
     */
    public long getCreated() {
        return created;
    }

    /**
     * @param created long, in ms
     */
    private void setCreated(long created) {
        this.created = created;
    }

    /**
     * @return String[] of Item comments
     */
    public String[] getComments() {
        return comments;
    }

    /**
     * @param comments String[] of Item comments
     */
    public void setComments(String[] comments) {
        this.comments = comments;
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
        return getCreated() == item.getCreated()
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
}
