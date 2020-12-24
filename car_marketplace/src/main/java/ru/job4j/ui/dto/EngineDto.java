package ru.job4j.ui.dto;

import ru.job4j.entity.Engine;
import ru.job4j.entity.enumerations.EngineType;

public class EngineDto {

    private Long id;

    private String model;

    private EngineType type;

    private Integer hp;

    private Integer volume;

    public EngineDto() {
    }

    public EngineDto(Long id,
                     String model,
                     EngineType type,
                     Integer hp,
                     Integer volume) {
        this.id = id;
        this.model = model;
        this.type = type;
        this.hp = hp;
        this.volume = volume;
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

    public EngineType getType() {
        return type;
    }

    public void setType(EngineType type) {
        this.type = type;
    }

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public static EngineDto fromEntity(Engine engine) {
        return new EngineDto(
                engine.getId(),
                engine.getModel(),
                engine.getType(),
                engine.getHp(),
                engine.getVolume()
        );
    }

    public Engine toEntity() {
        return Engine.create(
                this.getModel(),
                this.getHp(),
                this.getVolume(),
                this.getType()
        );
    }
}
