package ru.job4j.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import ru.job4j.DbUtil;
import ru.job4j.model.Account;
import ru.job4j.model.Ticket;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class OrderingServiceIntegrationTest {

    private static final Logger LOG = LogManager.getLogger(OrderingServiceIntegrationTest.class);

    private static final int HALL_SIZE = 9;

    private Ordering service = OrderingService.INSTANCE;
    private final DbUtil dbUtil = DbUtil.INSTANCE;

    private static final Account CUSTOMER = Account.newBuilder()
            .setFio("Аккаунтов Аккаунт Аккаунтович")
            .setPhone("+7 (999) 111-22-33")
            .build();

    @BeforeEach
    void setUp() {
        dbUtil.performMigrations();
    }

    @AfterEach
    void cleanUp() {
        dbUtil.cleanUp();
    }

    @Test
    void checkInitialHallState() {
        Map<Integer, List<Ticket>> tickets = service.getAllTickets();
        assertEquals(HALL_SIZE, tickets.values().stream().flatMap(List::stream).count());
        Predicate<Ticket> createdTickets = ticket -> ticket.getId() != null && !ticket.getOrdered();
        assertTrue(tickets.values()
                .stream()
                .flatMap(List::stream)
                .allMatch(createdTickets));
    }

    @Test
    void createOrderWithNewAccount() {
        Collection<Long> ticketIds = service.getAllTickets().values().stream()
                .findFirst().stream()
                .flatMap(List::stream)
                .map(Ticket::getId)
                .collect(Collectors.toSet());
        assertFalse(ticketIds.isEmpty());
        assertTrue(service.createOrder(ticketIds, CUSTOMER).isPresent());
        Predicate<Ticket> orderedTickets = ticket -> ticketIds.contains(ticket.getId()) && ticket.getOrdered();
        long ordered = service.getAllTickets().values().stream()
                .flatMap(List::stream)
                .filter(orderedTickets)
                .count();
        assertEquals(ticketIds.size(), ordered);
    }

    @Test
    void createOrderWithExistingAccount() {
        Map<Integer, List<Ticket>> tickets = service.getAllTickets();
        assertFalse(tickets.isEmpty());
        Collection<Long> firstOrder = tickets.get(1).stream().map(Ticket::getId).collect(Collectors.toSet());
        Collection<Long> secondOrder = tickets.get(2).stream().map(Ticket::getId).collect(Collectors.toSet());
        assertFalse(firstOrder.isEmpty() || secondOrder.isEmpty());
        assertTrue(service.createOrder(firstOrder, CUSTOMER).isPresent());
        assertTrue(service.createOrder(secondOrder, CUSTOMER).isPresent());
        assertTrue(service.getAllTickets().values().stream()
                .flatMap(List::stream)
                .filter(Ticket::getOrdered)
                .map(Ticket::getId)
                .allMatch(id -> firstOrder.contains(id) || secondOrder.contains(id))
        );
    }

    @Test
    void createOrderWithoutTickets() {
        assertTrue(service.createOrder(Collections.emptyList(), CUSTOMER).isEmpty());
    }

    @Test
    void orderWithBoughtTicketsShouldBeRejected() {
        Map<Integer, List<Ticket>> tickets = service.getAllTickets();
        assertFalse(tickets.isEmpty());
        Collection<Long> order = tickets.get(1).stream().map(Ticket::getId).collect(Collectors.toSet());
        assertTrue(service.createOrder(order, CUSTOMER).isPresent());
        Account anotherCustomer = Account.newBuilder().setFio("Иванов Иван Иванович").setPhone("+7 (888) 222-11-33").build();
        assertTrue(service.createOrder(order, anotherCustomer).isEmpty());
    }

}