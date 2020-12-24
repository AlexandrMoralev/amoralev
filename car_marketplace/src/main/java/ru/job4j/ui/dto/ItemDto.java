package ru.job4j.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.job4j.entity.Car;
import ru.job4j.entity.Item;
import ru.job4j.entity.User;
import ru.job4j.entity.enumerations.ItemType;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static java.util.Optional.ofNullable;
import static ru.job4j.util.DateTimeUtils.convertToString;

public class ItemDto {

    private Integer id;

    private ItemType type;

    private String createdAt;

    private UserDto createdBy;

    private Boolean isActive;

    private CarDto car;

    private Set<Long> photoIds = new HashSet<>();

    public ItemDto() {
    }

    public ItemDto(Integer id,
                   ItemType type,
                   String createdAt,
                   UserDto createdBy,
                   Boolean isActive,
                   CarDto car,
                   Set<Long> photoIds
    ) {
        this.id = id;
        this.type = type;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.isActive = isActive;
        this.car = car;
        this.photoIds.addAll(photoIds);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = convertToString(createdAt);
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public UserDto getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserDto createdBy) {
        this.createdBy = createdBy;
    }

    @JsonProperty(value = "isActive")
    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public CarDto getCar() {
        return car;
    }

    public void setCar(CarDto car) {
        this.car = car;
    }

    public Set<Long> getPhotoIds() {
        return photoIds;
    }

    public void setPhotoIds(Set<Long> photoIds) {
        this.photoIds.addAll(photoIds);
    }

    public static ItemDto fromEntity(Item item) {
        return new ItemDto(
                item.getId(),
                item.getType(),
                convertToString(item.getCreatedAt()),
                UserDto.fromEntity(item.getCreatedBy()),
                item.getActive(),
                CarDto.fromEntity(item.getCar()),
                item.getPhotoIds()
        );
    }

    public Item toEntity(User owner, Car car) {
        return Item.create(
                this.getType(),
                owner,
                car,
                ofNullable(isActive).orElse(true)
        );
    }
}
