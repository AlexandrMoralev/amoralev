package ru.job4j.service;

import ru.job4j.model.Account;
import ru.job4j.model.Ticket;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Ordering
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface Ordering {

    Map<Integer, List<Ticket>> getAllTickets();

    Optional<Long> createOrder(Collection<Long> ticketIds, Account customer);

}
