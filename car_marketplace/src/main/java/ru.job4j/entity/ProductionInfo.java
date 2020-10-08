package ru.job4j.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import ru.job4j.entity.enumerations.Color;
import ru.job4j.entity.enumerations.Make;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.StringJoiner;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "production_info", schema = "public")
public class ProductionInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "info_id", nullable = false, unique = true)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Make make;

    private LocalDateTime producedAt;

    @OneToOne
    @JoinTable
    private Engine engine;

    @OneToOne
    @JoinTable
    private Transmission transmission;

    @Enumerated(EnumType.STRING)
    private Color color;

    public ProductionInfo() {
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

    public LocalDateTime getProducedAt() {
        return producedAt;
    }

    public void setProducedAt(LocalDateTime producedAt) {
        this.producedAt = producedAt;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public Transmission getTransmission() {
        return transmission;
    }

    public void setTransmission(Transmission transmission) {
        this.transmission = transmission;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductionInfo)) {
            return false;
        }

        ProductionInfo that = (ProductionInfo) o;

        if (make != that.make) {
            return false;
        }
        if (!producedAt.equals(that.producedAt)) {
            return false;
        }
        if (!engine.equals(that.engine)) {
            return false;
        }
        if (!transmission.equals(that.transmission)) {
            return false;
        }
        return color == that.color;
    }

    @Override
    public int hashCode() {
        int result = make.hashCode();
        result = 31 * result + producedAt.hashCode();
        result = 31 * result + engine.hashCode();
        result = 31 * result + transmission.hashCode();
        result = 31 * result + color.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ProductionInfo.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("make=" + make)
                .add("producedAt=" + producedAt)
                .add("engine=" + engine)
                .add("transmission=" + transmission)
                .add("color=" + color)
                .toString();
    }
}
