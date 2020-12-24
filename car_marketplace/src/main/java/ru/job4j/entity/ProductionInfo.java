package ru.job4j.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NaturalId;
import ru.job4j.entity.enumerations.Color;
import ru.job4j.entity.enumerations.DriveType;
import ru.job4j.entity.enumerations.Make;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.StringJoiner;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "production_info", schema = "public")
public class ProductionInfo implements Serializable {

    private final static Logger LOG = LogManager.getLogger(ProductionInfo.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @NaturalId
    @Enumerated(EnumType.STRING)
    private Make make;

    @Column(name = "produced_at", nullable = false)
    private LocalDateTime producedAt;

    @OneToOne(targetEntity = Engine.class,
            optional = false,
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE}
    )
    @JoinColumn(unique = true)
    private Engine engine;

    @OneToOne(targetEntity = Transmission.class,
            optional = false,
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE}
    )
    @JoinColumn(unique = true)
    private Transmission transmission;

    @NaturalId
    @Enumerated(EnumType.STRING)
    private DriveType driveType;

    @NaturalId
    @Enumerated(EnumType.STRING)
    private Color color;

    public ProductionInfo() {
    }

    public static ProductionInfo create(Make make,
                                        LocalDateTime producedAt,
                                        Engine engine,
                                        Transmission transmission,
                                        DriveType driveType,
                                        Color color
    ) {
        ProductionInfo info = new ProductionInfo();
        info.setMake(make);
        info.setProducedAt(producedAt.truncatedTo(ChronoUnit.SECONDS));
        info.setEngine(engine);
        info.setTransmission(transmission);
        info.setDriveType(driveType);
        info.setColor(color);
        return info;
    }

    public Long getId() {
        return id;
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
        this.producedAt = producedAt.truncatedTo(ChronoUnit.SECONDS);
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
        if (!engine.equals(that.engine)) {
            return false;
        }
        if (!transmission.equals(that.transmission)) {
            return false;
        }
        if (!driveType.equals(that.driveType)) {
            return false;
        }
        if (color != that.color) {
            return false;
        }
        return producedAt.truncatedTo(ChronoUnit.SECONDS).equals(that.producedAt.truncatedTo(ChronoUnit.SECONDS));
    }

    @Override
    public int hashCode() {
        int result = make.hashCode();
        result = 31 * result + producedAt.truncatedTo(ChronoUnit.SECONDS).hashCode();
        result = 31 * result + engine.hashCode();
        result = 31 * result + transmission.hashCode();
        result = 31 * result + (driveType != null ? driveType.hashCode() : 0);
        result = 31 * result + (color != null ? color.hashCode() : 0);
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
                .add("driveType=" + driveType)
                .add("color=" + color)
                .toString();
    }
}
