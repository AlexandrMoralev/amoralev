package ru.job4j.controllers;

import net.jcip.annotations.ThreadSafe;
import ru.job4j.crudservlet.User;
import ru.job4j.servlet.Dispatcher;
import ru.job4j.servlet.ValidationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * UsersController
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@ThreadSafe
public class UsersController extends HttpServlet {
    private final ValidationService logic = ValidationService.INSTANCE;
    private final Dispatcher dispatcher = Dispatcher.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Collection<User> list = logic.findAll();
        req.setAttribute("userList", list);
        req.getRequestDispatcher("/WEB-INF/views/users-view.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/result-page-view.jsp?result="
                + (dispatcher.execute(req) ? "1" : "0"))
                .forward(req, resp);
    }
}

