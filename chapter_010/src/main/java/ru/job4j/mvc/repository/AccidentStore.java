package ru.job4j.mvc.repository;

import ru.job4j.mvc.model.Accident;

import java.util.Collection;
import java.util.Optional;

public interface AccidentStore {

    Optional<Accident> getAccident(Integer id);

    Collection<Accident> getAllAccidents();

    Accident addAccident(Accident accident);

    void updateAccident(Accident updatedAccident);

    void removeAccident(Integer id);
}
