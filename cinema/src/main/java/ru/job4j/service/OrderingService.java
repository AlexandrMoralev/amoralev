package ru.job4j.service;

import ru.job4j.model.Account;
import ru.job4j.model.Ticket;
import ru.job4j.persistence.DBStore;
import ru.job4j.persistence.Store;

import java.util.Collection;

public enum OrderingService implements Ordering {
    INSTANCE;

    private final Validation validator = ValidationService.INSTANCE;
    private final Store store = DBStore.INSTANCE;

    OrderingService() {
    }

    @Override
    public Collection<Ticket> getAllTickets() {
        return store.getAllTickets();
    }

    @Override
    public boolean createOrder(Collection<Integer> ticketIds, Account customer) {
        return validator.isAccountValid(customer) && store.createOrder(ticketIds, customer);
    }
}
