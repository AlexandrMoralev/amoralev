package ru.job4j.service;

import ru.job4j.exception.OrderValidationException;
import ru.job4j.model.Account;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public enum ValidationService implements Validation {
    INSTANCE;

    private final Pattern FIO_VALIDATOR = Pattern.compile("/^[А-ЯA-Z][а-яa-zА-ЯA-Z\\-]{0,}\\s[А-ЯA-Z][а-яa-zА-ЯA-Z\\-]{1,}(\\s[А-ЯA-Z][а-яa-zА-ЯA-Z\\-]{1,})?$/");
    private final Pattern PHONE_VALIDATOR = Pattern.compile("/^\\d[\\d\\(\\)\\ -]{4,14}\\d$/");
    private final int hallSize = 9; // TODO get from properties

    ValidationService() {
    }

    private final Function<Account, String> checkNull = account -> account != null ? "" : "error";
    private final Function<Account, String> checkId = account -> (account.getId() == null || account.getId() > 0) ? "" : "id";
    private final Function<Account, String> checkFio = account -> FIO_VALIDATOR.matcher(account.getFio()).matches() ? "" : "fio";
    private final Function<Account, String> checkPhone = account -> PHONE_VALIDATOR.matcher(account.getPhone()).matches() ? "" : "phone";

    private final Predicate<String> failedCheck = msg -> !msg.isBlank();

    private final Predicate<Integer> invalidIdCheck = ticketId -> ticketId < 0 || ticketId > hallSize;

    @Override
    public void validateAccount(Account account) throws OrderValidationException {
        Optional<String> errorMessage = List.of(checkNull, checkId, checkFio, checkPhone).stream()
                .map(check -> check.apply(account))
                .filter(failedCheck)
                .findFirst();
        if (errorMessage.isPresent()) {
            throw new OrderValidationException(String.format(ACCOUNT_INVALID, errorMessage.get()));
        }
    }

    @Override
    public void validateTickets(Collection<Integer> ticketIds) throws OrderValidationException {
        Optional<Integer> invalidTicketId = ticketIds.stream()
                .filter(invalidIdCheck)
                .findFirst();
        if (invalidTicketId.isPresent()) {
            throw new OrderValidationException(String.format(TICKET_INVALID, "error"));
        }
    }

}
