package ru.job4j.ui.dto;

import ru.job4j.entity.User;

import java.time.LocalDateTime;

import static ru.job4j.util.DateTimeUtils.convertToString;

public class UserDto {

    private Integer id;

    private String phone;

    private String name;

    private String registeredAt;

    public UserDto() {
    }

    public UserDto(Integer id,
                   String phone,
                   String name,
                   String registeredAt
    ) {
        this.id = id;
        this.phone = phone;
        this.name = name;
        this.registeredAt = registeredAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = convertToString(registeredAt);
    }

    public static UserDto fromEntity(User user) {
        return new UserDto(
                user.getId(),
                user.getPhone(),
                user.getName(),
                convertToString(user.getRegisteredAt())
        );
    }

}
