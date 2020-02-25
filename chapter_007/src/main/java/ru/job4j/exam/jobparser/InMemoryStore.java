package ru.job4j.exam.jobparser;

import net.jcip.annotations.ThreadSafe;
import org.joda.time.LocalDateTime;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
public class InMemoryStore implements Store<Vacancy> {

    private final ConcurrentHashMap<String, Vacancy> store;
    private final AtomicInteger key = new AtomicInteger(1);;

    public InMemoryStore() {
        this.store = new ConcurrentHashMap<>(1000);
    }

    @Override
    public Optional<String> add(Vacancy vacancy) {
        String id = String.valueOf(key.getAndIncrement());
        this.store.putIfAbsent(id, vacancy);
        return Optional.empty();
    }

    @Override
    public Collection<String> addAll(Collection<Vacancy> e) {
        return null;
    }

    @Override
    public boolean update(Vacancy vacancy) {
        return false;
    }

    @Override
    public int delete(String id) {
        return 0;
    }

    @Override
    public int deleteAll() {
        return 0;
    }

    @Override
    public int deleteByPeriod(LocalDateTime fromDate, LocalDateTime toDate) {
        return 0;
    }

    @Override
    public Optional<Vacancy> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<Vacancy> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public Collection<Vacancy> findAll() {
        return null;
    }

    @Override
    public Collection<Vacancy> findByPeriod(LocalDateTime fromDate, LocalDateTime toDate) {
        return null;
    }

    @Override
    public Collection<Vacancy> findRecent(int count) {
        return null;
    }
}
