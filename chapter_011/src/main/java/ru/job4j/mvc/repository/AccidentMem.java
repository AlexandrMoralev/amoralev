package ru.job4j.mvc.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.mvc.model.Accident;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Optional.ofNullable;

@Repository
public class AccidentMem implements AccidentStore {

    private final AtomicInteger index;
    private final Map<Integer, Accident> store;

    public AccidentMem() {
        this.store = new HashMap<>() { {
            // TODO remove stub data
            put(1, Accident.newBuilder().setId(1).setName("name 1").setText("text 1").setAddress("address 1").build());
            put(2, Accident.newBuilder().setId(2).setName("name 2").setText("text 2").setAddress("address 2").build());
            put(3, Accident.newBuilder().setId(3).setName("name 3").setText("text 3").setAddress("address 3").build());
        } };
        this.index = new AtomicInteger(1);
    }

    @Override
    public Optional<Accident> getAccident(Integer id) {
        return ofNullable(this.store.get(id));
    }

    @Override
    public Collection<Accident> getAllAccidents() {
        return new ArrayList<>(this.store.values());
    }

    @Override
    public Accident addAccident(Accident accident) {
        if (accident.getId() == null) {
            Accident acc = Accident.of(accident).setId(index.getAndIncrement()).build();
            return this.store.put(acc.getId(), acc);
        }
        return null;
    }

    @Override
    public void updateAccident(Accident updatedAccident) {
        this.store.replace(updatedAccident.getId(), updatedAccident);
    }

    @Override
    public void removeAccident(Integer id) {
        this.store.remove(id);
    }
}
