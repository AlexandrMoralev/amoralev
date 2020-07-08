package ru.job4j.mvc.service;

import org.springframework.stereotype.Service;
import ru.job4j.mvc.model.Accident;
import ru.job4j.mvc.repository.AccidentMem;

import java.util.Collection;

@Service
public class AccidentService {

    private AccidentMem store;

    public AccidentService(AccidentMem store) {
        this.store = store;
    }

    public Collection<Accident> getAccidents() {
        return store.getAllAccidents();
    }
}
