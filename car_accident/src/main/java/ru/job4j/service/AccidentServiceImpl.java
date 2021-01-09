package ru.job4j.service;

import org.springframework.stereotype.Service;
import ru.job4j.model.Accident;
import ru.job4j.repository.AccidentStore;

import java.util.Collection;
import java.util.Optional;

@Service
public class AccidentServiceImpl implements AccidentService {

    private final AccidentStore store;

    public AccidentServiceImpl(AccidentStore store) {
        this.store = store;
    }

    @Override
    public Accident saveAccident(Accident accident) {
        return store.addAccident(accident);
    }

    @Override
    public Collection<Accident> getAllAccidents() {
        return store.getAllAccidents();
    }

    @Override
    public Optional<Accident> getAccident(Integer id) {
        return store.getAccident(id);
    }


    @Override
    public void updateAccident(Accident updatedAccident) {
        store.updateAccident(updatedAccident);
    }

    @Override
    public void removeAccident(Integer id) {
        store.removeAccident(id);
    }

}
