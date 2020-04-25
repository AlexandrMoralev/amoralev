package ru.job4j.service;

import ru.job4j.model.Account;
import ru.job4j.model.Ticket;

import java.util.Collection;
import java.util.Optional;

/**
 * Ordering
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface Ordering {

    Collection<Ticket> getAllTickets();

    Optional<Long> createOrder(Collection<Integer> ticketIds, Account customer);

}
