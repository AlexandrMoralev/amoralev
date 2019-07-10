package ru.job4j.servlet;

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
    private final ValidationService logic;

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
        return request -> {
            String name = request.getParameter("name");
            String login = request.getParameter("login");
            String email = request.getParameter("email");
            return logic.add(name, login, email);
        };
    }

    public Function<HttpServletRequest, Boolean> update() {
        return request -> {
            int userId = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            String login = request.getParameter("login");
            String email = request.getParameter("email");
            return logic.update(userId, name, login, email);
        };
    }

    public Function<HttpServletRequest, Boolean> delete() {
        return request ->
                logic.delete(
                        Integer.parseInt(
                                request.getParameter("id"))
                );
    }

    public Function<HttpServletRequest, Boolean> noAction() {
        return request -> true;
    }
}

