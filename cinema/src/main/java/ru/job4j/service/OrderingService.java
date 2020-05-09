package ru.job4j.service;

import net.jcip.annotations.ThreadSafe;
import ru.job4j.model.Account;
import ru.job4j.model.Ticket;
import ru.job4j.persistence.DBStore;
import ru.job4j.persistence.Store;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@ThreadSafe
public enum OrderingService implements Ordering {
    INSTANCE;

    private Store store;

    OrderingService() {
        this.store = DBStore.INSTANCE;
    }

    @Override
    public Map<Integer, List<Ticket>> getAllTickets() {
        return store.getAllTickets()
                .stream()
                .collect(Collectors.groupingBy(Ticket::getRow,
                        toSortedList(Comparator.comparingInt(Ticket::getSeat))));
    }

    @Override
    public Optional<Long> createOrder(Collection<Long> ticketIds, Account customer) {
         return store.createOrder(ticketIds, customer);
    }


    private <T> Collector<T, ?, List<T>> toSortedList(Comparator<? super T> comparator) {
        return Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(comparator)), ArrayList::new);
    }
}
