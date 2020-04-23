package ru.job4j.service;

import ru.job4j.model.Account;
import ru.job4j.model.Ticket;

/**
 * Validation
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface Validation {

    boolean isAccountValid(Account account);

    boolean isTicketValid(Ticket ticket);
}
