package ru.job4j.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import ru.job4j.entity.enumerations.TransmissionType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.StringJoiner;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "transmissions", schema = "public")
public class Transmission implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transmission_id", unique = true, nullable = false)
    private Long id;

    @Column(name = "model", nullable = false)
    private String model;

    @Enumerated(EnumType.STRING)
    private TransmissionType type;

    public Transmission() {
    }

    public static Transmission create(String model,
                                      TransmissionType type
    ) {
        Transmission transmission = new Transmission();
        transmission.setModel(model);
        transmission.setType(type);
        return transmission;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transmission)) {
            return false;
        }

        Transmission that = (Transmission) o;

        if (!model.equals(that.model)) {
            return false;
        }
        return type == that.type;
    }

    @Override
    public int hashCode() {
        int result = model.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Transmission.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("model='" + model + "'")
                .add("type=" + type)
                .toString();
    }
}
