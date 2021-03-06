package ru.job4j.servlet;

import net.jcip.annotations.ThreadSafe;
import ru.job4j.crudservlet.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * UserUpdateServlet
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@ThreadSafe
public class UserUpdateServlet extends HttpServlet {
    private final ValidationService logic = ValidationService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<User> optionalUser = logic.findById(Integer.parseInt(req.getParameter("id")));
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        if (optionalUser.isPresent()) {
            req.setAttribute("user", optionalUser.get());
            getServletContext()
                    .getRequestDispatcher("/jsp/user-update.jsp")
                    .forward(req, resp);
        } else {
            getServletContext()
                    .getRequestDispatcher("/jsp/actions-result-page.jsp")
                    .forward(req, resp);
        }
    }
}