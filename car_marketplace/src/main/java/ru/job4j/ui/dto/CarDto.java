package ru.job4j.ui.dto;

import ru.job4j.entity.Car;
import ru.job4j.entity.ProductionInfo;
import ru.job4j.entity.enumerations.BodyStyle;

public class CarDto {

    private Long id;

    private ProductionInfoDto productionInfo;

    private BodyStyle bodyStyle;

    private Integer mileage;

    private Long price;

    public CarDto() {
    }

    public CarDto(Long id,
                  ProductionInfoDto productionInfo,
                  BodyStyle bodyStyle,
                  Integer mileage,
                  Long price
    ) {
        this.id = id;
        this.productionInfo = productionInfo;
        this.bodyStyle = bodyStyle;
        this.mileage = mileage;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductionInfoDto getProductionInfo() {
        return productionInfo;
    }

    public void setProductionInfo(ProductionInfoDto productionInfo) {
        this.productionInfo = productionInfo;
    }

    public BodyStyle getBodyStyle() {
        return bodyStyle;
    }

    public void setBodyStyle(BodyStyle bodyStyle) {
        this.bodyStyle = bodyStyle;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public static CarDto fromEntity(Car car) {
        return new CarDto(
                car.getId(),
                ProductionInfoDto.fromEntity(car.getProductionInfo()),
                car.getBodyStyle(),
                car.getMileage(),
                car.getPrice()
        );
    }

    public Car toEntity(ProductionInfo productionInfo) {
        return Car.create(
                productionInfo,
                this.getBodyStyle(),
                this.getMileage(),
                this.getPrice()
        );
    }
}
