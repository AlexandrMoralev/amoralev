package ru.job4j.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.exception.OrderValidationException;
import ru.job4j.model.Account;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public enum ValidationService implements Validation {
    INSTANCE;

    private static final Logger LOG = LogManager.getLogger(ValidationService.class);
    private final Pattern fioValidator = Pattern.compile("^[А-я]+ [А-я]+ [А-я]+$");
    //    private final Pattern phoneValidator = Pattern.compile("/\\+7\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2}/"); // TODO fix phone regexp
    private final int hallSize = 9; // TODO get from properties

    ValidationService() {
    }

    private final Function<Account, String> checkNull = account -> account != null ? "" : ERROR;
    private final Function<Account, String> checkId = account -> (account.getId() != null && account.getId() > 0) ? "" : "id";
    private final Function<Account, String> checkFio = account -> account.getFio() != null && fioValidator.matcher(account.getFio()).matches() ? "" : "fio";
    //    private final Function<Account, String> checkPhone = account -> account.getPhone() != null && phoneValidator.matcher(account.getPhone()).matches() ? "" : "phone";
    private final Predicate<String> simplePhonePredicate = phone -> phone.chars().noneMatch(Character::isLetter);
    private final Function<Account, String> checkPhone = account -> account.getPhone() != null && simplePhonePredicate.test(account.getPhone()) ? "" : "phone";

    private final Predicate<String> failedCheck = msg -> !msg.isBlank();

    private final Predicate<Integer> invalidIdCheck = ticketId -> ticketId != null && (ticketId <= 0 || ticketId > hallSize);

    @Override
    public void validateAccount(Account account) throws OrderValidationException {
        Optional<String> errorMessage = List.of(checkNull, checkId, checkFio, checkPhone).stream()
                .map(check -> check.apply(account))
                .filter(failedCheck)
                .findFirst();
        if (errorMessage.isPresent()) {
            generateException(
                    String.format(ACCOUNT_INVALID, errorMessage.get())
            );
        }
    }

    @Override
    public void validateTickets(Collection<Integer> ticketIds) throws OrderValidationException {
        if (ticketIds == null || ticketIds.isEmpty()) {
            generateException(
                    String.format(TICKET_INVALID, "empty")
            );
        } else if (new HashSet<>(ticketIds).size() != ticketIds.size()) {
            generateException(
                    String.format(TICKET_INVALID, "duplication")
            );
        } else {
            Optional<Integer> invalidTicketId = ticketIds.stream()
                    .filter(invalidIdCheck)
                    .findFirst();
            if (invalidTicketId.isPresent()) {
                generateException(
                        String.format(TICKET_INVALID, String.format("id.%s", invalidTicketId.get()))
                );
            }
        }
    }

    private static void generateException(String message) throws OrderValidationException {
        LOG.error(message);
        throw new OrderValidationException(message);
    }

}
