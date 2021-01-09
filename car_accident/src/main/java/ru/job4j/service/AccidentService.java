package ru.job4j.service;

import ru.job4j.model.Accident;

import java.util.Collection;
import java.util.Optional;

public interface AccidentService {

    Accident saveAccident(Accident accident);

    Collection<Accident> getAllAccidents();

    Optional<Accident> getAccident(Integer id);

    void updateAccident(Accident updatedAccident);

    void removeAccident(Integer id);
}
