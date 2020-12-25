package ru.job4j.ui.dto;

import ru.job4j.entity.Photo;

public class PhotoDto {

    private Long id;

    private String name;

    public PhotoDto() {
    }

    public PhotoDto(Long id,
                    String name
    ) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static PhotoDto fromEntity(Photo photo) {
        return new PhotoDto(
                photo.getId(),
                photo.getName()
        );
    }
}
