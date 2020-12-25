package ru.job4j.ui.dto;

import ru.job4j.entity.Engine;
import ru.job4j.entity.ProductionInfo;
import ru.job4j.entity.Transmission;
import ru.job4j.entity.enumerations.Color;
import ru.job4j.entity.enumerations.DriveType;
import ru.job4j.entity.enumerations.Make;

import java.time.LocalDateTime;

import static ru.job4j.util.DateTimeUtils.convertToLocalDateTime;
import static ru.job4j.util.DateTimeUtils.convertToString;

public class ProductionInfoDto {

    private Long id;

    private Make make;

    private String producedAt;

    private EngineDto engine;

    private TransmissionDto transmission;

    private DriveType driveType;

    private Color color;

    public ProductionInfoDto() {
    }

    public ProductionInfoDto(Long id,
                             Make make,
                             String producedAt,
                             EngineDto engine,
                             TransmissionDto transmission,
                             DriveType driveType,
                             Color color
    ) {
        this.id = id;
        this.make = make;
        this.producedAt = producedAt;
        this.engine = engine;
        this.transmission = transmission;
        this.driveType = driveType;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Make getMake() {
        return make;
    }

    public void setMake(Make make) {
        this.make = make;
    }

    public String getProducedAt() {
        return producedAt;
    }

    public void setProducedAt(LocalDateTime producedAt) {
        this.producedAt = convertToString(producedAt);
    }

    public void setProducedAt(String producedAt) {
        this.producedAt = producedAt;
    }

    public EngineDto getEngine() {
        return engine;
    }

    public void setEngine(EngineDto engine) {
        this.engine = engine;
    }

    public TransmissionDto getTransmission() {
        return transmission;
    }

    public void setTransmission(TransmissionDto transmission) {
        this.transmission = transmission;
    }

    public DriveType getDriveType() {
        return driveType;
    }

    public void setDriveType(DriveType driveType) {
        this.driveType = driveType;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public static ProductionInfoDto fromEntity(ProductionInfo info) {
        return new ProductionInfoDto(
                info.getId(),
                info.getMake(),
                convertToString(info.getProducedAt()),
                EngineDto.fromEntity(info.getEngine()),
                TransmissionDto.fromEntity(info.getTransmission()),
                info.getDriveType(),
                info.getColor()
        );
    }

    public ProductionInfo toEntity(Engine engine, Transmission transmission) {
        return ProductionInfo.create(
                this.getMake(),
                convertToLocalDateTime(this.getProducedAt()),
                engine,
                transmission,
                this.getDriveType(),
                this.getColor()
        );
    }
}
