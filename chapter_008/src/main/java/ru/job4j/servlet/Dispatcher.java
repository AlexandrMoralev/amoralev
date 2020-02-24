package ru.job4j.servlet;

import ru.job4j.crudservlet.User;
import ru.job4j.crudservlet.ValidationService;
import ru.job4j.filtersecurity.Role;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Dispatcher
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public enum Dispatcher {
    INSTANCE;
    private final Map<String, Function<HttpServletRequest, Boolean>> dispatcher;
    private final Validation<User> logic;

    Dispatcher() {
        this.dispatcher = new HashMap<>();
        this.logic = ValidationService.INSTANCE;
        this.dispatcher.put("create", create());
        this.dispatcher.put("update", update());
        this.dispatcher.put("delete", delete());
        this.dispatcher.put("noAction", noAction());
    }

    public boolean execute(HttpServletRequest req) {
        Function<HttpServletRequest, Boolean> func = dispatcher.get(req.getParameter("action"));
        return func != null ? func.apply(req) : false;
    }

    public Function<HttpServletRequest, Boolean> create() {
        return request ->
                logic.add(new User.Builder()
                        .setName(request.getParameter("name"))
                        .setLogin(request.getParameter("login"))
                        .setEmail(request.getParameter("email"))
                        .setPassword(request.getParameter("password"))
                        .setRole(Role.valueOf(request.getParameter("role").toUpperCase()))
                        .build()
                ).isPresent();
    }

    public Function<HttpServletRequest, Boolean> update() {
        return request ->
                logic.update(new User.Builder()
                        .setId(Integer.parseInt(request.getParameter("id")))
                        .setName(request.getParameter("name"))
                        .setLogin(request.getParameter("login"))
                        .setEmail(request.getParameter("email"))
                        .setPassword(request.getParameter("password"))
                        .setRole(Role.valueOf(request.getParameter("role").toUpperCase()))
                        .build());
    }

    public Function<HttpServletRequest, Boolean> delete() {
        return request ->
                logic.delete(Integer.parseInt(request.getParameter("id")));
    }

    public Function<HttpServletRequest, Boolean> noAction() {
        return request -> true;
    }
}

