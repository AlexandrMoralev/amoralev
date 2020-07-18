package ru.job4j.mvc.model;

import java.util.Objects;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public class Accident {

    private final Integer id;
    private final String name;
    private final String text;
    private final String address;

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

    public static Accident.Builder newBuilder() {
        return new Accident.Builder();
    }

    public static class Builder {

        private Integer id;
        private String name;
        private String text;
        private String address;

        public Builder of(Accident accident) {
            ofNullable(accident.getId()).ifPresent(v -> this.id = v);
            Optional.of(accident.getName()).ifPresent(v -> this.name = v);
            Optional.of(accident.getText()).ifPresent(v -> this.text = v);
            Optional.of(accident.getAddress()).ifPresent(v -> this.address = v);
            return this;
        }

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
