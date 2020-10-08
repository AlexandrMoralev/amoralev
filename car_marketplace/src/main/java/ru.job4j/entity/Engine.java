package ru.job4j.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "engines", schema = "public")
public class Engine implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "engine_id", unique = true, nullable = false)
    private Long id;

    @Column(name = "model", unique = true, nullable = false)
    private String model;

    @Column(name = "hp", nullable = false)
    private Integer hp;

    @Column(name = "cost", nullable = false)
    private Integer cost;

    public Engine() {
    }

    public static Engine create(String model,
                                int hp,
                                int cost
    ) {
        Engine engine = new Engine();
        engine.setModel(model);
        engine.setHp(hp);
        engine.setCost(cost);
        return engine;
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

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
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
        return getModel().equals(engine.getModel())
                && getHp().equals(engine.getHp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getModel(), getHp());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Engine.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("model='" + model + "'")
                .add("hp=" + hp)
                .add("cost=" + cost)
                .toString();
    }
}
