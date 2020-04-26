package ru.job4j.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.exception.OrderValidationException;
import ru.job4j.model.Account;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static ru.job4j.service.Validation.*;

class ValidationServiceTest {

    private static final Logger LOG = LogManager.getLogger(ValidationServiceTest.class);
    private final Validation service = ValidationService.INSTANCE;

    private static final Long ACC_ID = 1L;
    private static final String ACC_FIO = "Петров Петр Петрович";
    private static final String ACC_PHONE = "+7(111)000-00-00";

    private static Account baseAccount;

    private static Map<String, Account> accountsWithInvalidFields;
    private static Map<String, Account> accountsWithNulls;
    private static Set<Collection<Integer>> ticketsWithInvalidIds;

    @BeforeAll
    static void init() {
        baseAccount = Account.newBuilder().setId(ACC_ID).setFio(ACC_FIO).setPhone(ACC_PHONE).build();
        accountsWithNulls = Map.of(
                accError("id"), Account.newBuilder().of(baseAccount).setId(null).build(),
                accError("fio"), Account.newBuilder().of(baseAccount).setFio(null).build(),
                accError("phone"), Account.newBuilder().of(baseAccount).setPhone(null).build()
        );
        accountsWithInvalidFields = Map.of(
                accError("id"), Account.newBuilder().of(baseAccount).setId((long) -1).build(),
                accError("fio"), Account.newBuilder().of(baseAccount).setFio("Иваныч").build(),
                accError("phone"), Account.newBuilder().of(baseAccount).setPhone("8333s44455").build()
        );
        ticketsWithInvalidIds = Set.of(
                List.of(1, 0),
                List.of(1, -2),
                List.of(1, Integer.MAX_VALUE)
        );
    }

    @Test
    void testValidAccountShouldPassChecks() {
        try {
            service.validateAccount(baseAccount);
        } catch (OrderValidationException e) {
            LOG.debug(e.getMessage());
            fail();
        }
    }

    @Test
    void testNullAccountShouldFailCheck() {
        Exception exception = assertThrows(OrderValidationException.class, () -> service.validateAccount(null));
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(accError(ERROR)));
    }

    @Test
    void testAccountFieldsValidationErrors() {
        Collection<String> errors = accountsWithInvalidFields.keySet();
        Set<String> actualErrors = new HashSet<>();
        errors.forEach(expectedMessage -> {
            Exception exception = assertThrows(OrderValidationException.class, () -> {
                String key = expectedMessage;
                service.validateAccount(accountsWithInvalidFields.get(key));
            });
            actualErrors.add(exception.getMessage());
        });
        assertTrue(actualErrors.containsAll(errors));

    }

    @Test
    void testAccountWithNullFieldValidationErrors() {
        Collection<String> errors = accountsWithNulls.keySet();
        Set<String> actualErrors = new HashSet<>();
        errors.forEach(expectedMessage -> {
            Exception exception = assertThrows(OrderValidationException.class, () -> {
                String key = expectedMessage;
                service.validateAccount(accountsWithNulls.get(key));
            });
            actualErrors.add(exception.getMessage());
        });
        assertTrue(actualErrors.containsAll(errors));
    }

    @Test
    void testValidTicketIdsShouldPassChecks() {
        try {
            service.validateTickets(List.of(1, 2, 3));
        } catch (OrderValidationException e) {
            fail();
        }
    }

    @Test
    void testNullTicketIdsShouldFailCheck() {
        Exception exception = assertThrows(OrderValidationException.class, () -> service.validateTickets(null));
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(ticketError("empty")));
    }

    @Test
    void testEmptyTicketIdsShouldFailCheck() {
        Exception exception = assertThrows(OrderValidationException.class, () -> service.validateTickets(Collections.emptyList()));
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(ticketError("empty")));
    }

    @Test
    void testTicketIdsValidationErrors() {
        String idError = ticketError("id");
        ticketsWithInvalidIds.forEach(invalidTicketIds -> {
            Exception exception = assertThrows(OrderValidationException.class, () -> service.validateTickets(invalidTicketIds));
            String actualMessage = exception.getMessage();
            assertTrue(actualMessage.contains(idError));
        });
    }

    @Test
    void testDuplicateTicketIdsShouldCauseValidationErrors() {
        Exception exception = assertThrows(OrderValidationException.class, () -> service.validateTickets(List.of(1, 2, 1)));
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(ticketError("duplication")));
    }

    private static String accError(String code) {
        return String.format(ACCOUNT_INVALID, code);
    }

    private static String ticketError(String code) {
        return String.format(TICKET_INVALID, code);
    }
}