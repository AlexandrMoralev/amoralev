package ru.job4j.service;

import ru.job4j.model.Account;
import ru.job4j.model.Ticket;

import java.util.Collection;

/**
 * Ordering
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface Ordering {

    Collection<Ticket> getAllTickets();

    boolean createOrder(Collection<Integer> ticketIds, Account customer);

}
