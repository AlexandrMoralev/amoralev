package ru.job4j.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import ru.job4j.entity.enumerations.ItemType;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "items", schema = "public")
public class Item implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id", unique = true, nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private ItemType type;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User createdBy;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @OneToOne(targetEntity = Car.class,
            optional = false,
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE}
    )
    @JoinColumn(unique = true)
    private Car car;            // TODO extract common Product interface by ItemType

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Long> photoIds = new HashSet<>();

    // TODO add item description

    public Item() {
        this.createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    public static Item create(ItemType type,
                              User createdBy,
                              Car car,
                              boolean isActive
    ) {
        Item item = new Item();
        item.setType(type);
        item.setCreatedBy(createdBy);
        item.setCar(car);
        item.setCreatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        item.setActive(isActive);
        return item;
    }

    public Integer getId() {
        return id;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt.truncatedTo(ChronoUnit.SECONDS);
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Set<Long> getPhotoIds() {
        return photoIds;
    }

    public void setPhotoIds(Set<Long> photoIds) {
        this.photoIds = photoIds;
    }

    public void addPhotoIds(Collection<Long> photoIds) {
        this.photoIds.addAll(photoIds);
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

        if (type != item.type) {
            return false;
        }
        if (!Objects.equals(createdAt, item.createdAt)) {
            return false;
        }
        if (!Objects.equals(createdBy, item.createdBy)) {
            return false;
        }
        return Objects.equals(car, item.car);
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 31;
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
        result = 31 * result + (car != null ? car.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Item.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("type=" + type)
                .add("createdAt=" + createdAt)
                .add("createdBy=" + createdBy)
                .add("isActive=" + isActive)
                .toString();
    }
}
