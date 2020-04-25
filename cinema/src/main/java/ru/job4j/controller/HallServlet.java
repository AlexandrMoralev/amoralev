package ru.job4j.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.exception.OrderValidationException;
import ru.job4j.model.Account;
import ru.job4j.service.Ordering;
import ru.job4j.service.OrderingService;
import ru.job4j.service.Validation;
import ru.job4j.service.ValidationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Optional;

/**
 * HallServlet
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class HallServlet extends HttpServlet {

    private static final Logger LOG = LogManager.getLogger(HallServlet.class);
    private static final String OK = "{\"message\":\"success\"}";
    private static final String ERROR = "{\"message\":\"%s\"}";
    private static final String SERVER_ERROR = "server error";

    private final Ordering orderingService = OrderingService.INSTANCE;
    private final Validation validationService = ValidationService.INSTANCE;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = request.getReader();
        String str;
        while ((str = br.readLine()) != null) {
            sb.append(str);
        }
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(sb.toString());
        Account account = mapper.readValue(jsonNode.get("account").asText(), Account.class);
        Collection<Integer> ticketIds = mapper.readValue(
                jsonNode.get("ticketIds").asText(),
                new TypeReference<>() {
                });
        validateOrder(response, account, ticketIds);
        orderingService.createOrder(ticketIds, account).ifPresent(orderId -> sendData(OK, response));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Optional.ofNullable(request.getParameter("tickets"))
                .ifPresent(t -> sendData(orderingService.getAllTickets(), response));
    }

    private void validateOrder(HttpServletResponse response, Account account, Collection<Integer> ticketIds) {
        try {
            validationService.validateAccount(account);
            validationService.validateTickets(ticketIds);
        } catch (OrderValidationException e) {
            LOG.error(e.getMessage());
            sendData(String.format(ERROR, e.getMessage()), response);
        }
    }

    private void sendData(Object object, HttpServletResponse response) {
        try (PrintWriter writer = response.getWriter()
        ) {
            ObjectMapper mapper = new ObjectMapper();
            writer.println(mapper.writeValueAsString(object));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(SERVER_ERROR);
        }
    }

    private void sendData(Collection objects, HttpServletResponse response) {
        try (PrintWriter writer = response.getWriter();
             StringWriter sw = new StringWriter()
        ) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(sw, objects);
            writer.println(sw.toString());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(SERVER_ERROR);
        }
    }

}
