package ru.job4j.mapping.modelsrelations.annotationbased.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "car", schema = "public")
@DynamicInsert
@DynamicUpdate
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id", unique = true, nullable = false)
    private Long id;

    @Column(name = "brand", nullable = false)
    private String brand;

    @ManyToOne
    @JoinColumn(name = "engine_id", foreignKey = @ForeignKey(name = "ENGINE_ID_FK"))
    private Engine engine;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "history_owner",
            joinColumns = {
                    @JoinColumn(
                            name = "driver_id",
                            nullable = false,
                            updatable = false)
            },
            inverseJoinColumns = {
                    @JoinColumn(
                            name = "car_id",
                            nullable = false,
                            updatable = false)
            }
    )
    private Set<Driver> owners = new HashSet<>();

    public Car() {
    }

    public static Car create(String brand,
                             Engine engine,
                             Collection<Driver> owners
    ) {
        Car car = new Car();
        car.setBrand(brand);
        car.setEngine(engine);
        car.getOwners().addAll(owners);
        return car;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public Set<Driver> getOwners() {
        return owners;
    }

    public void setOwners(Set<Driver> owners) {
        this.owners = owners;
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
        return getId().equals(car.getId())
                && getBrand().equals(car.getBrand())
                && getEngine().equals(car.getEngine());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getBrand(), getEngine());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Car.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("brand='" + brand + "'")
                .add("engine=" + engine)
                .add("owners=" + owners)
                .toString();
    }
}
