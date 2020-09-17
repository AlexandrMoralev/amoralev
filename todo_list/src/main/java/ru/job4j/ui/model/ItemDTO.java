package ru.job4j.ui.model;

import ru.job4j.domain.Item;

import java.util.Optional;

import static java.util.Optional.ofNullable;

public class ItemDTO {

    private Integer id;
    private String description;
    private Long created;
    private boolean done;

    public ItemDTO() {
    }

    public ItemDTO(Integer id,
                   String description,
                   Long created,
                   boolean done) {
        this.id = id;
        this.description = description;
        this.created = created;
        this.done = done;
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

    public boolean getDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Item toModel() {
        Item.Builder item = Item.newBuilder();
        ofNullable(this.id).ifPresent(item::setId);
        ofNullable(this.description).ifPresent(item::setDescription);
        ofNullable(this.created).ifPresent(item::setCreated);
        Optional.of(this.done).ifPresent(item::setDone);
        return item.build();
    }

    public static ItemDTO fromModel(Item item) {
        return new ItemDTO(
                item.getId(),
                item.getDescription(),
                item.getCreated(),
                item.isDone()
        );
    }
    // TODO isValid
}
