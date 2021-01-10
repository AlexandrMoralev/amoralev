package ru.job4j.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.model.Accident;
import ru.job4j.model.AccidentType;
import ru.job4j.model.Rule;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Optional.ofNullable;

@Repository
public class AccidentMem implements AccidentStore {

    private final AtomicInteger index;
    private final Map<Integer, Accident> store;

    public AccidentMem() {
        this.index = new AtomicInteger(1);
        // TODO remove stub data
        this.store = new HashMap<>() {
            {
                put(index.getAndIncrement(),
                        Accident.newBuilder()
                                .setId(1)
                                .setName("name 1")
                                .setText("text 1")
                                .setAddress("address 1")
                                .setType(AccidentType.of(1, "Две машины"))
                                .setRules(List.of(Rule.of(1, "Статья. 1")))
                                .build()
                );
                put(index.getAndIncrement(),
                        Accident.newBuilder()
                                .setId(2)
                                .setName("name 2")
                                .setText("text 2")
                                .setAddress("address 2")
                                .setType(AccidentType.of(2, "Машина и человек"))
                                .setRules(List.of(Rule.of(2, "Статья. 2")))
                                .build()
                );
                put(index.getAndIncrement(),
                        Accident.newBuilder()
                                .setId(3)
                                .setName("name 3")
                                .setText("text 3")
                                .setAddress("address 3")
                                .setType(AccidentType.of(3, "Машина и велосипед"))
                                .setRules(List.of(
                                        Rule.of(1, "Статья. 1"),
                                        Rule.of(2, "Статья. 2"),
                                        Rule.of(3, "Статья. 3"))
                                )
                                .build()
                );
            }
        };

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
