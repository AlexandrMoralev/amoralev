package ru.job4j.persistence;

import ru.job4j.model.Account;
import ru.job4j.model.Ticket;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

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
        return Collections.emptyList();
    }

    @Override
    public Optional<Long> createOrder(Collection<Integer> ticketIds, Account customer) {
        return Optional.empty();
    }
}
