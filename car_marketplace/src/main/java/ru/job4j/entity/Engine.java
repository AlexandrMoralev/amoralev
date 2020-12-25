package ru.job4j.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import ru.job4j.entity.enumerations.EngineType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "engines", schema = "public")
public class Engine implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "model", unique = true, nullable = false)
    private String model;

    @Enumerated(EnumType.STRING)
    private EngineType type;

    @Column(name = "hp", nullable = false)
    private Integer hp;

    @Column(name = "volume", nullable = false)
    private Integer volume;

    public Engine() {
    }

    public static Engine create(String model,
                                int hp,
                                int volume,
                                EngineType type
    ) {
        Engine engine = new Engine();
        engine.setModel(model);
        engine.setHp(hp);
        engine.setVolume(volume);
        engine.setType(type);
        return engine;
    }

    public Long getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public EngineType getType() {
        return type;
    }

    public void setType(EngineType type) {
        this.type = type;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Engine)) {
            return false;
        }

        Engine engine = (Engine) o;

        if (!model.equals(engine.model)) {
            return false;
        }
        if (type != engine.type) {
            return false;
        }
        return hp.equals(engine.hp);
    }

    @Override
    public int hashCode() {
        int result = model.hashCode();
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + hp.hashCode();
        return result;
    }
}
