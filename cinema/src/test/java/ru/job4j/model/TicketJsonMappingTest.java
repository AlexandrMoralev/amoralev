package ru.job4j.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class TicketJsonMappingTest {

    private Ticket firstTicket;

    private Ticket secondTicket;

    private ObjectMapper mapper;


    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        firstTicket = Ticket.newBuilder()
                .setId(1L)
                .setRow(1)
                .setSeat(1)
                .setPrice(200)
                .setOrdered(false)
                .build();
        secondTicket = Ticket.newBuilder()
                .setId(2L)
                .setRow(2)
                .setSeat(1)
                .setPrice(100)
                .setOrdered(true)
                .build();
    }

    @Test
    void testOneTicketJsonMapping() throws JsonProcessingException {

        String ticketAsJsonString = mapper.writeValueAsString(firstTicket);

        JsonNode jsonNode = mapper.readTree(ticketAsJsonString);
        assertEquals(jsonNode.get("id").asLong(), firstTicket.getId());
        assertEquals(jsonNode.get("row").asInt(), firstTicket.getRow());
        assertEquals(jsonNode.get("seat").asInt(), firstTicket.getSeat());
        assertEquals(jsonNode.get("price").asInt(), firstTicket.getPrice());
        assertEquals(jsonNode.get("ordered").asBoolean(), firstTicket.getOrdered());

        Ticket ticketFromJson = mapper.readValue(ticketAsJsonString, Ticket.class);
        assertEquals(ticketFromJson.getId(), firstTicket.getId());
        assertEquals(ticketFromJson.getRow(), firstTicket.getRow());
        assertEquals(ticketFromJson.getSeat(), firstTicket.getSeat());
        assertEquals(ticketFromJson.getPrice(), firstTicket.getPrice());
        assertEquals(ticketFromJson.getOrdered(), firstTicket.getOrdered());

        assertEquals(ticketFromJson, firstTicket);
    }

    @Test
    void testTicketsCollectionJsonMapping() throws IOException {

        Collection<Ticket> tickets = List.of(firstTicket, secondTicket);

        StringWriter sw = new StringWriter();
        mapper.writeValue(sw, tickets);

        String ticketsAsJson = sw.toString();

        JsonNode jsonNode = mapper.readTree(ticketsAsJson);

        Collection<Ticket> ticketsFromJson = Arrays.asList(mapper.treeToValue(jsonNode, Ticket[].class));

        assertIterableEquals(tickets, ticketsFromJson);
    }

}