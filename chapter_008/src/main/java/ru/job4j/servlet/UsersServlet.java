package ru.job4j.servlet;

import net.jcip.annotations.ThreadSafe;
import ru.job4j.crudservlet.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * UsersServlet
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@ThreadSafe
public class UsersServlet extends HttpServlet {
    private final ValidationService logic = ValidationService.INSTANCE;
    private final Dispatcher dispatcher = Dispatcher.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        Collection<User> list = logic.findAll();
        req.setAttribute("userList", list);
        getServletContext()
                .getRequestDispatcher("/jsp/all-users.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        resp.sendRedirect(req.getContextPath() + "/jsp/actions-result-page.jsp?result="
                + (dispatcher.execute(req) ? "1" : "0"));
    }
}

