package ru.job4j.servlet;

import net.jcip.annotations.ThreadSafe;
import ru.job4j.crudservlet.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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
        Optional<User> optionalUser = logic.findById(
                Integer.parseInt(
                        req.getParameter("id"))
        );
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        StringBuilder builder = new StringBuilder("<!DOCTYPE html> <html lang=\"en\">"
                + "<head><meta charset=\"UTF-8\">"
                + "<title>Update user</title>"
                + "</head><body>"
                + "<h1>Update user</h1><br>");
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            builder.append("<form action='").append(req.getContextPath()).append("/list' method='post'>")
                    .append("Name : <input type='text' name='name' value='").append(user.getName()).append("'/><br>")
                    .append("Login : <input type='text' name='login' value='").append(user.getLogin()).append("'/><br>")
                    .append("E-mail : <input type='text' name='email' value='").append(user.getEmail()).append("'/><br>")
                    .append("<input type='hidden' name='id' value='").append(user.getId()).append("'>")
                    .append("<input type='submit' name='action' value='update'><br>")
                    .append("</form>");
        } else {
            builder.append("<meta http-equiv='refresh' content='2; url=").append(req.getContextPath()).append("/list'></head>").append("<body>Invalid ID");
        }
        builder.append("</body></html>");
        writer.write(builder.toString());
        writer.flush();
    }
}