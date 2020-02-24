package ru.job4j.servlet;

import net.jcip.annotations.ThreadSafe;
import ru.job4j.crudservlet.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        Collection<User> list = logic.findAll();
        StringBuilder builder = new StringBuilder("<!DOCTYPE html> <html lang=\"en\">"
                + "<head><meta charset=\"UTF-8\">"
                + "<title>All users</title>"
                + "</head><body><h1>All users</h1><br><table>");
        for (User user : list) {
            builder.append(String.format("<tr><td>id : %d, Name : %s, Login : %s, e-mail : %s,  date : %s</td>",
                    user.getId(), user.getName(), user.getLogin(), user.getEmail(), user.getCreated()))
                    .append("<td><form>")
                    .append("<button formaction='").append(req.getContextPath())
                    .append("/update").append("' formmethod='get' name='id' value='")
                    .append(user.getId()).append("'>Update</button>")
                    .append("<button formaction='").append(req.getContextPath())
                    .append("/list?action=delete&id=").append(user.getId())
                    .append("' formmethod='post'>Delete</button>")
                    .append("</form></td></tr>");
        }
        builder.append("</table><br><form>")
                .append("<button formaction='").append(req.getContextPath()).append("/create' formmethod='get'>Create user</button>")
                .append("</form></body></html>");
        writer.write(builder.toString());
        writer.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        writer.append("<!DOCTYPE html> <html lang='en'>" + "<head><meta charset='UTF-8'>" + "<title>Result</title>" + "<meta http-equiv='refresh' content='2; url=")
                .append(req.getContextPath()).append("/list'>").append("</head><body>")
                .append(dispatcher.execute(req) ? "Action done" : "Action error")
                .append("</body></html>");
        writer.flush();
    }
}

