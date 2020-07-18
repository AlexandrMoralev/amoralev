package ru.job4j.mvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.job4j.mvc.model.Accident;
import ru.job4j.mvc.repository.AccidentStore;

import java.util.Collection;

@Service
public class AccidentService {

    private AccidentStore store;

    @Autowired
    public AccidentService(AccidentStore store) {
        this.store = store;
    }

    public Collection<Accident> getAccidents() {
        return store.getAllAccidents();
    }
}
