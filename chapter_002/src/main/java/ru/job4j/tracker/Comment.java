package ru.job4j.tracker;

import java.util.Objects;

/**
 * Comment
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Comment {
    private int id;
    private String comment;
    private long itemId;

    public Comment(final String comment) {
        this.comment = comment;
        this.id = -1;
        this.itemId = -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(final int itemId) {
        this.itemId = itemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Comment)) {
            return false;
        }
        Comment comment1 = (Comment) o;
        return getId() == comment1.getId()
                && getItemId() == comment1.getItemId()
                && Objects.equals(getComment(), comment1.getComment());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getComment(), getItemId());
    }

    @Override
    public String toString() {
        return "Comment{"
                + "comment='" + comment + '\''
                + '}';
    }
}
