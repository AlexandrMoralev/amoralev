package ru.job4j.persistence;

import ru.job4j.model.Account;
import ru.job4j.model.Ticket;

import java.util.Collection;

/**
 * DBStore
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public enum DBStore implements Store {
    INSTANCE;

    DBStore() {
    }

    @Override
    public Collection<Ticket> getAllTickets() {
        return null;
    }

    @Override
    public boolean createOrder(Collection<Integer> ticketIds, Account customer) {
        return false;
    }
}
