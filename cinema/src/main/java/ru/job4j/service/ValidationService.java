package ru.job4j.service;

import ru.job4j.model.Account;
import ru.job4j.model.Ticket;

public enum ValidationService implements Validation {
    INSTANCE;

    ValidationService() {
    }

    @Override
    public boolean isAccountValid(Account account) {
        return false;
    }

    @Override
    public boolean isTicketValid(Ticket ticket) {
        return false;
    }
}
