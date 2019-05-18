package ru.job4j.servlet;

import net.jcip.annotations.ThreadSafe;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * UserCreateServlet
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@ThreadSafe
public class UserCreateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        StringBuilder builder = new StringBuilder("<!DOCTYPE html> <html lang=\"en\">"
                + "<head><meta charset=\"UTF-8\">"
                + "<title>Create user</title>"
                + "</head><body><h1>Create user</h1><br>")
                .append("<form action='").append(req.getContextPath()).append("/list' method='post'>")
                .append("Name : <input type='text' name='name'/><br>")
                .append("Login : <input type='text' name='login'/><br>")
                .append("e-mail : <input type='text' name='email'/><br>")
                .append("<input type='submit' name='action' value='create'>")
                .append("</form>")
                .append("</body></html>");
        writer.write(builder.toString());
        writer.flush();
    }
}