package ru.job4j.service;

import ru.job4j.exception.OrderValidationException;
import ru.job4j.model.Account;

import java.util.Collection;

/**
 * Validation
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface Validation {

    String ACCOUNT_INVALID = "invalid.account.%s";
    String TICKET_INVALID = "invalid.ticket.%s";

    void validateAccount(Account account) throws OrderValidationException;

    void validateTickets(Collection<Integer> ticketIds) throws OrderValidationException;
}
