package ru.job4j.controllers;

import net.jcip.annotations.ThreadSafe;
import ru.job4j.crudservlet.Address;
import ru.job4j.crudservlet.User;
import ru.job4j.crudservlet.ValidationService;
import ru.job4j.servlet.Validation;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * UsersController
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@ThreadSafe
public class AddressController extends HttpServlet {
    private final Validation<User> logic = ValidationService.INSTANCE;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletOutputStream outputStream = resp.getOutputStream();
        getParameter.apply("addressId", req)
                .ifPresent(sendAddress(outputStream));
        getParameter.apply("countries", req)
                .map(Boolean::parseBoolean)
                .filter(value -> value)
                .ifPresent(sendAllCountries(outputStream));
        getParameter.apply("country", req)
                .ifPresent(sendAddressesInCountry(outputStream));
    }

    private Consumer<String> sendAddressesInCountry(ServletOutputStream outputStream) {
        return name -> {
            PrintWriter writer = new PrintWriter(outputStream);
            writer.write(logic.getAddressesInCountry(name).stream()
                    .map(Address::toString)
                    .collect(Collectors.joining(";")));
            writer.flush();
        };
    }

    private Consumer<Boolean> sendAllCountries(ServletOutputStream outputStream) {
        return countries -> {
            PrintWriter writer = new PrintWriter(outputStream);
            writer.write(String.join(",", logic.getAllCountries()));
            writer.flush();
        };
    }

    private Consumer<String> sendAddress(ServletOutputStream outputStream) {
        return addressId -> {
            PrintWriter writer = new PrintWriter(outputStream);
            writer.write(logic.getAddress(Integer.parseInt(addressId))
                    .map(Address::toString)
                    .orElse(""));
            writer.flush();
        };
    }

    private static BiFunction<String, HttpServletRequest, Optional<String>> getParameter =
            (paramName, request) -> Optional.ofNullable(request.getParameter(paramName)).map(String::strip).filter(v -> !v.isBlank());
}

