package ru.job4j.controllers;

import ru.job4j.crudservlet.User;
import ru.job4j.crudservlet.ValidationService;
import ru.job4j.servlet.Validation;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

//import ru.job4j.servlet.ValidationService;

/**
 * UserUpdateController
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class UserUpdateController extends HttpServlet {
    private final Validation<User> logic = ValidationService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<User> optionalUser = logic.findById(Integer.parseInt(req.getParameter("userId")));
        if (optionalUser.isPresent()) {
            req.setAttribute("user", optionalUser.get());
            req.getRequestDispatcher("/WEB-INF/views/user-update-view.jsp")
                    .forward(req, resp);
        } else {
            req.setAttribute("action", "noAction");
            req.setAttribute("msg", "no user found");
            req.getRequestDispatcher("/WEB-INF/views/result-page-view.jsp")
                    .forward(req, resp);
        }
    }
}