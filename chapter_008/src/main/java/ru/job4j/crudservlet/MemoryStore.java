package ru.job4j.crudservlet;

import net.jcip.annotations.ThreadSafe;
import ru.job4j.filtersecurity.Role;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

/**
 * MemoryStore - persistence layout
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@ThreadSafe
public enum MemoryStore implements Store<User> {
    INSTANCE;
    private final AtomicInteger userIdCounter = new AtomicInteger(1);
    private final AtomicInteger addressIdCounter = new AtomicInteger(1);
    private final ConcurrentHashMap<Integer, User> users;
    private final ConcurrentHashMap<Integer, Address> addresses;

    MemoryStore() {
        this.users = new ConcurrentHashMap<>();
        this.addresses = new ConcurrentHashMap<>();
        addInitData();
    }

    private void addInitData() {
        AtomicInteger index = new AtomicInteger(0);
        Address rootAddress = Address.newBuilder().setId(index.get()).setCountry("Russia").setCity("Spb").build();
        this.users.put(index.get(),
                User.newBuilder()
                        .setId(0)
                        .setName("root")
                        .setLogin("root@root.ru")
                        .setCreated("at the dawn of a new era")
                        .setPassword("root")
                        .setRole(Role.ROOT)
                        .setAddress(rootAddress)
                        .build()
        );
        this.addresses.put(index.get(), rootAddress);

        Address usaAddress = Address.newBuilder().setId(index.incrementAndGet()).setCountry("USA").setCity("NY").build();
        this.addresses.put(index.get(), usaAddress);
        Address ukAddress = Address.newBuilder().setId(index.incrementAndGet()).setCountry("UK").setCity("London").build();
        this.addresses.put(index.get(), ukAddress);
    }

    @Override
    public Optional<Integer> add(User user) {
        Optional<Integer> userId = Optional.empty();
        if (!this.users.contains(user)) {
            Address address = user.getAddress();
            int id = userIdCounter.getAndIncrement();
            if (address.getId() != null && addresses.get(address.getId()).equals(address)) {
                this.users.put(id, user);
            } else {
                int addressId = addressIdCounter.getAndIncrement();
                Address storedAddress = Address.newBuilder().of(address).setId(addressId).build();
                this.addresses.put(addressId, storedAddress);
                this.users.put(id, User.newBuilder().of(user).setAddress(storedAddress).build());
            }
            userId = Optional.of(id);
        }
        return userId;
    }

    @Override
    public boolean update(User user) {
        Address address = user.getAddress();
        if (address.getId() != null  && addresses.get(address.getId()).equals(address)) {
            return this.users.replace(user.getId(), user) != null;
        } else {
            int addressId = addressIdCounter.getAndIncrement();
            Address storedAddress = Address.newBuilder().of(address).setId(addressId).build();
            this.addresses.put(addressId, storedAddress);
            return this.users.replace(user.getId(), User.newBuilder().of(user).setAddress(storedAddress).build()) != null;
        }
    }

    @Override
    public void delete(int userId) {
        this.users.remove(userId);
    }

    @Override
    public Collection<User> findAll() {
        return getUsersBy(allPredicate());
    }

    @Override
    public Optional<User> findById(int userId) {
        User cachedUser = this.users.get(userId);
        return cachedUser == null
                ? Optional.empty()
                : Optional.of(User.newBuilder().of(cachedUser).setId(userId).build());
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return this.users.entrySet().stream()
                .filter(entry -> login.equals(entry.getValue().getLogin()))
                .findFirst()
                .map(extractUser());
    }

    @Override
    public Collection<User> findByCountry(String country) {
        return getUsersBy(countryPredicate(country));
    }

    @Override
    public Collection<User> findByCity(String city) {
        return getUsersBy(cityPredicate(city));
    }

    @Override
    public Optional<Address> getAddress(int id) {
        return ofNullable(this.addresses.get(id));
    }

    @Override
    public Collection<String> getAllCountries() {
        return this.addresses.values().stream().map(Address::getCountry).collect(Collectors.toSet());
    }

    @Override
    public Collection<Address> getAddressesInCountry(String country) {
        return this.addresses.values().stream().filter(a -> a.getCountry().equalsIgnoreCase(country)).collect(Collectors.toSet());
    }

    @Override
    public boolean isCredential(String login, String password) {
        return this.users.values().stream().anyMatch(user -> login.equals(user.getLogin()) && password.equals(user.getPassword()));
    }

    private Collection<User> getUsersBy(Predicate<Map.Entry<Integer, User>> predicate) {
        return this.users.entrySet().stream()
                .filter(predicate)
                .map(extractUser())
                .collect(Collectors.toList());
    }

    private Function<Map.Entry<Integer, User>, User> extractUser() {
        return entry -> User.newBuilder().of(entry.getValue()).setId(entry.getKey()).build();
    }

    private Predicate<Map.Entry<Integer, User>> allPredicate() {
        return entry -> true;
    }

    private Predicate<Map.Entry<Integer, User>> countryPredicate(String country) {
        return entry -> entry.getValue().getAddress().getCountry().equalsIgnoreCase(country);
    }

    private Predicate<Map.Entry<Integer, User>> cityPredicate(String city) {
        return entry -> entry.getValue().getAddress().getCity().equalsIgnoreCase(city);
    }

}