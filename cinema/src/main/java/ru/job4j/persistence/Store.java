package ru.job4j.persistence;

import ru.job4j.model.Account;
import ru.job4j.model.Ticket;

import java.util.Collection;
import java.util.Optional;

/**
 * Store
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface Store {

    Collection<Ticket> getAllTickets();

    Optional<Long> createOrder(Collection<Long> ticketIds, Account customer);
}
