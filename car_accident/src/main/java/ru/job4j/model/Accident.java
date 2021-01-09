package ru.job4j.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Entity
@Table(name = "accident")
public class Accident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    private String name;
    private String text;
    private String address;

    public Accident(Integer id,
                    String name,
                    String text,
                    String address
    ) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.address = address;
    }

    private Accident(Accident.Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.text = builder.text;
        this.address = builder.address;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Accident)) {
            return false;
        }
        Accident accident = (Accident) o;
        return Objects.equals(getId(), accident.getId())
                && Objects.equals(getName(), accident.getName())
                && Objects.equals(getText(), accident.getText())
                && Objects.equals(getAddress(), accident.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getText(), getAddress());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Accident{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", text='").append(text).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static Accident.Builder of(Accident accident) {
        Accident.Builder builder = newBuilder();
        ofNullable(accident.getId()).ifPresent(builder::setId);
        Optional.of(accident.getName()).ifPresent(builder::setName);
        Optional.of(accident.getText()).ifPresent(builder::setText);
        Optional.of(accident.getAddress()).ifPresent(builder::setAddress);
        return builder;
    }

    public static Accident.Builder newBuilder() {
        return new Accident.Builder();
    }

    public static class Builder {

        private Integer id;
        private String name;
        private String text;
        private String address;

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Accident build() {
            return new Accident(this);
        }
    }
}
