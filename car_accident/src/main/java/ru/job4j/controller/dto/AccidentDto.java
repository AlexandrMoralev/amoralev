package ru.job4j.controller.dto;

import ru.job4j.model.Accident;
import ru.job4j.model.AccidentType;
import ru.job4j.model.Rule;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

public class AccidentDto {

    private Integer id;
    private String name;
    private String text;
    private String address;
    private Integer typeId;
    private Collection<Integer> ruleIds;

    public AccidentDto(Integer id,
                       String name,
                       String text,
                       String address,
                       Integer typeId,
                       Collection<Integer> ruleIds
    ) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.address = address;
        this.typeId = typeId;
        this.ruleIds = ruleIds;
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

    public Integer getTypeId() {
        return typeId;
    }

    public Collection<Integer> getRuleIds() {
        return ruleIds;
    }

    public Accident toEntity(List<AccidentType> types, List<Rule> rules) {
        Accident.Builder entity = Accident.newBuilder();
        ofNullable(this.id).ifPresent(entity::setId);
        ofNullable(this.name).ifPresent(entity::setName);
        ofNullable(this.text).ifPresent(entity::setText);
        ofNullable(this.address).ifPresent(entity::setAddress);
        ofNullable(this.typeId).flatMap(id -> types.stream().filter(t -> id.equals(t.getId())).findFirst()).ifPresent(entity::setType);
        entity.setRules(rules);
        return entity.build();
    }

    public static AccidentDto fromEntity(Accident accident) {
        return new AccidentDto(
                accident.getId(),
                accident.getName(),
                accident.getText(),
                accident.getAddress(),
                accident.getType().getId(),
                accident.getRules().stream().map(Rule::getId).collect(Collectors.toList())
        );
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
                && Objects.equals(getAddress(), accident.getAddress())
                && Objects.equals(getTypeId(), accident.getTypeId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getText(), getAddress(), getTypeId());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AccidentDto{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", text='").append(text).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", typeId='").append(typeId).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
