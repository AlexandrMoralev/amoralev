package ru.job4j.exam.jobparser;

import net.jcip.annotations.ThreadSafe;
import org.joda.time.LocalDateTime;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@ThreadSafe
public class InMemoryStore implements Store<Vacancy> {

    private final ConcurrentHashMap<String, Vacancy> store;
    private final AtomicInteger key;

    private static final Comparator<Vacancy> DATE_TIME_COMPARATOR = Comparator.comparing(Vacancy::getCreated, Comparator.nullsLast(Comparator.reverseOrder()));

    public InMemoryStore(final Config config) {
        this.key = new AtomicInteger(config.getInt("parser.init_page_number"));
        this.store = new ConcurrentHashMap<>(config.getInt("parser.init_store_capacity"));
    }

    @Override
    public Optional<String> add(Vacancy vacancy) {
        String id = String.valueOf(key.getAndIncrement());
        Vacancy newVacancy = Vacancy.newBuilder().of(vacancy).setId(id).build();
        if (this.store.putIfAbsent(id, newVacancy) != null) {
            return Optional.empty();
        } else {
            return Optional.of(id);
        }
    }

    @Override
    public void addAll(Collection<Vacancy> vacancies) {
        vacancies.stream()
                .map(v -> Vacancy.newBuilder().of(v).setId(String.valueOf(key.getAndIncrement())).build())
                .forEach(v -> this.store.putIfAbsent(v.getId(), v));
    }

    @Override
    public boolean update(Vacancy vacancy) {
        return this.store.replace(vacancy.getId(), vacancy) != null;
    }

    @Override
    public boolean delete(String id) {
        return this.store.remove(id) != null;
    }

    @Override
    public void deleteAll() {
        this.store.clear();
    }

    @Override
    public int deleteByPeriod(LocalDateTime fromDate, LocalDateTime toDate) {
        List<String> ids = this.store.values().stream()
                .filter(createdWithinInterval(fromDate, toDate))
                .map(Vacancy::getId)
                .collect(Collectors.toList());
        ids.stream().forEach(this.store::remove);
        return ids.size();
    }

    @Override
    public Optional<Vacancy> findById(String id) {
        return Optional.ofNullable(this.store.get(id));
    }

    @Override
    public Collection<Vacancy> findByName(String name) {
        return this.store.values().stream()
                .filter(v -> v.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Vacancy> findAll() {
        return this.store.values();
    }

    @Override
    public Collection<Vacancy> findByPeriod(LocalDateTime fromDate, LocalDateTime toDate) {
        return this.store.values().stream()
                .filter(createdWithinInterval(fromDate, toDate))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Vacancy> findRecent(int count) {
        return this.store.values().stream()
                .sorted(DATE_TIME_COMPARATOR)
                .limit(count)
                .collect(Collectors.toList());
    }

    private Predicate<Vacancy> createdWithinInterval(LocalDateTime fromDate, LocalDateTime toDate) {
        return v -> {
            LocalDateTime created = v.getCreated();
            return !(created.isBefore(fromDate) || created.isAfter(toDate));
        };
    }
}
