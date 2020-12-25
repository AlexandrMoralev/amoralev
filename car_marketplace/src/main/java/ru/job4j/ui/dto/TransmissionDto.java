package ru.job4j.ui.dto;

import ru.job4j.entity.Transmission;
import ru.job4j.entity.enumerations.TransmissionType;

public class TransmissionDto {

    private Long id;

    private String model;

    private TransmissionType type;

    public TransmissionDto() {
    }

    public TransmissionDto(Long id,
                           String model,
                           TransmissionType type
    ) {
        this.id = id;
        this.model = model;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public TransmissionType getType() {
        return type;
    }

    public void setType(TransmissionType type) {
        this.type = type;
    }

    public static TransmissionDto fromEntity(Transmission transmission) {
        return new TransmissionDto(
                transmission.getId(),
                transmission.getModel(),
                transmission.getType()
        );
    }

    public Transmission toEntity() {
        return Transmission.create(
                this.getModel(),
                this.getType()
        );
    }
}
