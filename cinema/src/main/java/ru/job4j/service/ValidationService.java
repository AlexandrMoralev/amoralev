package ru.job4j.service;

import net.jcip.annotations.ThreadSafe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.Config;
import ru.job4j.exception.OrderValidationException;
import ru.job4j.model.Account;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@ThreadSafe
public enum ValidationService implements Validation {
    INSTANCE;

    private static final Logger LOG = LogManager.getLogger(ValidationService.class);
    private final Config config = Config.INSTANCE;

    private final Pattern fioValidator = Pattern.compile("^[А-я]+ [А-я]+ [А-я]+$");
    private final Integer hallSize = config.getInt("hall.size").orElse(null);

    ValidationService() {
    }

    @Override
    public void validateAccount(Account account) throws OrderValidationException {

        Function<Account, String> checkNull = acc -> acc != null ? "" : ERROR;
        Function<Account, String> checkId = acc -> acc.getId() == null || acc.getId() > 0 ? "" : "id";
        Function<Account, String> checkFio = acc -> acc.getFio() != null && fioValidator.matcher(acc.getFio()).matches() ? "" : "fio";
        Function<Account, String> checkPhone = acc -> acc.getPhone() != null && acc.getPhone().chars().noneMatch(Character::isLetter) ? "" : "phone";
        Predicate<String> failedCheck = msg -> !msg.isBlank();

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
    public void validateTickets(Collection<Long> ticketIds) throws OrderValidationException {

        Predicate<Long> invalidIdCheck = ticketId -> ticketId != null && (ticketId <= 0 || ticketId > hallSize);

        if (ticketIds == null || ticketIds.isEmpty()) {
            generateException(
                    String.format(TICKET_INVALID, "empty")
            );
        } else if (new HashSet<>(ticketIds).size() != ticketIds.size()) {
            generateException(
                    String.format(TICKET_INVALID, "duplication")
            );
        } else {
            Optional<Long> invalidTicketId = ticketIds.stream()
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
