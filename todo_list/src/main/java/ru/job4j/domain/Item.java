package ru.job4j.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;
    private Long created;
    private boolean done;


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
                && Objects.equals(getId(), item.getId())
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
}
