package ru.job4j.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NaturalId;
import ru.job4j.entity.enumerations.BodyStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "cars", schema = "public")
public class Car implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @NaturalId
    @OneToOne(targetEntity = ProductionInfo.class,
            optional = false,
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE}
    )
    @JoinColumn(unique = true)
    private ProductionInfo productionInfo;

    @Enumerated(EnumType.STRING)
    private BodyStyle bodyStyle;

    @NaturalId(mutable = true)
    @Column(name = "mileage", nullable = false)
    private Integer mileage;

    @NaturalId(mutable = true)
    @Column(name = "price", nullable = false)
    private Long price;

    public Car() {
    }

    public static Car create(ProductionInfo productionInfo,
                             BodyStyle bodyStyle,
                             Integer mileage,
                             Long price
    ) {
        Car car = new Car();
        car.setProductionInfo(productionInfo);
        car.setBodyStyle(bodyStyle);
        car.setMileage(mileage);
        car.setPrice(price);
        return car;
    }

    public Long getId() {
        return id;
    }

    public ProductionInfo getProductionInfo() {
        return productionInfo;
    }

    public void setProductionInfo(ProductionInfo productionInfo) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Car)) {
            return false;
        }

        Car car = (Car) o;

        if (bodyStyle != car.bodyStyle) {
            return false;
        }
        if (!Objects.equals(mileage, car.mileage)) {
            return false;
        }
        if (!Objects.equals(price, car.price)) {
            return false;
        }
        return Objects.equals(productionInfo, car.productionInfo);
    }

    @Override
    public int hashCode() {
        int result = productionInfo != null ? productionInfo.hashCode() : 0;
        result = 31 * result + (bodyStyle != null ? bodyStyle.hashCode() : 0);
        result = 31 * result + (mileage != null ? mileage.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Car.class.getSimpleName() + "[", "]")
                .add("productionInfo=" + productionInfo)
                .add("bodyStyle=" + bodyStyle)
                .add("mileage=" + mileage)
                .add("price=" + price)
                .toString();
    }
}
