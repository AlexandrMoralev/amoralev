package ru.job4j.controller.dto;

import ru.job4j.model.Accident;

import java.util.Objects;

import static java.util.Optional.ofNullable;

public class AccidentDto {

    private Integer id;
    private String name;
    private String text;
    private String address;

    public AccidentDto(Integer id,
                       String name,
                       String text,
                       String address
    ) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.address = address;
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

    public Accident toEntity() {
        Accident.Builder entity = Accident.newBuilder();
        ofNullable(this.id).ifPresent(entity::setId);
        ofNullable(this.name).ifPresent(entity::setName);
        ofNullable(this.text).ifPresent(entity::setText);
        ofNullable(this.address).ifPresent(entity::setAddress);
        return entity.build();
    }

    public static AccidentDto fromEntity(Accident accident) {
        return new AccidentDto(accident.getId(), accident.getName(), accident.getText(), accident.getAddress());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccidentDto)) {
            return false;
        }
        AccidentDto accident = (AccidentDto) o;
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
        final StringBuilder sb = new StringBuilder("AccidentDto{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", text='").append(text).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
