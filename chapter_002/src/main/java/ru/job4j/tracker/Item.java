package ru.job4j.tracker;

/**
 * Item
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @since $Id$
 * @version 0.1
 */
public class Item {
    private String id;
    private String name;
    private String description;
    private long created;
    private String[] comments;

    /**
     * Instance constructor of the Item class
     * @param name String name of the Item
     * @param description String short description of the Item
     * created long, in ms - date of Item creation
     */
    Item(String name, String description) {
        this.name = name;
        this.description = description;
        this.created = System.currentTimeMillis();
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
    void setId(String id) {
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
}
