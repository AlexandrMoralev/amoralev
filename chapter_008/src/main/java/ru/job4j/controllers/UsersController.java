package ru.job4j.controllers;

import net.jcip.annotations.ThreadSafe;
import ru.job4j.crudservlet.User;
import ru.job4j.crudservlet.ValidationService;
import ru.job4j.servlet.Dispatcher;
import ru.job4j.servlet.Validation;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * UsersController
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@ThreadSafe
public class UsersController extends HttpServlet {
    private final Validation<User> logic = ValidationService.INSTANCE;
    private final Dispatcher actionDispatcher = Dispatcher.INSTANCE;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("userList", logic.findAll());
        req.getRequestDispatcher("/WEB-INF/views/users-view.jsp")
                .forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/result-page-view.jsp?result="
                + (actionDispatcher.execute(req) ? "Action was completed successfully " : "Action error"))
                .forward(req, resp);
    }
}

