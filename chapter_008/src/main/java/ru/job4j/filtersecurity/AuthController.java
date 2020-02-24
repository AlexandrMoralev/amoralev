package ru.job4j.filtersecurity;

import ru.job4j.servlet.ValidationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthController extends HttpServlet {
    private final ValidationService logic = ValidationService.INSTANCE;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login-view.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        if (logic.isCredential(login, password)) {
            HttpSession session = req.getSession();
            synchronized (session) {
                session.setAttribute("login", login);
                req.setAttribute("error", "");
                req.setAttribute("action", "noAction");
                logic.findByLogin(login).ifPresent(user -> session.setAttribute("principal", user));
                resp.sendRedirect(String.format("%s/users", req.getContextPath()));
            }
        } else {
            req.setAttribute("error", "Invalid credentials");
            doGet(req, resp);
        }
    }
}
