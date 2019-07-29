package ru.job4j.crudservlet;

import net.jcip.annotations.ThreadSafe;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * UserServlet - presentation layout
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@ThreadSafe
public class UserServlet extends HttpServlet {
    private final ValidationService logic;
    private final Dispatcher dispatcher;

    // Workaround - keeping the User immutable
    private final Store store;

    public UserServlet() {
        this.logic = ValidationService.INSTANCE;
        this.dispatcher = new Dispatcher();
        this.store = MemoryStore.INSTANCE;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        String response = this.buildResponse(this.logic.findAll());
        writer.write(response);
        writer.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        String success = "Action completed successfully";
        String reject = "Action was not performed";
        String answer = this.dispatcher.execute(req.getParameter("action"), req)
                ? success
                : reject;
        writer.write(answer);
        writer.flush();
    }

    private String buildResponse(Collection<User> users) {
        if (users == null || users.isEmpty()) {
            return "empty response";
        }
        StringBuilder stringBuilder = new StringBuilder();
        String separator = System.lineSeparator();
        for (User user : users) {
            stringBuilder.append(user.toString()).append(separator);
        }
        return stringBuilder.toString();
    }

    private class Dispatcher {
        private final Map<String, Function<ServletRequest, Boolean>> dispatcher;

        Dispatcher() {
            this.dispatcher = new LinkedHashMap<>();
            this.dispatcher.put("add", add());
            this.dispatcher.put("update", update());
            this.dispatcher.put("delete", delete());
        }

        public boolean execute(String actionKey, ServletRequest request) {
            Function<ServletRequest, Boolean> actionFunc = this.dispatcher.get(actionKey);
            return actionFunc == null ? false : actionFunc.apply(request);
        }

        private Function<ServletRequest, Boolean> add() {
            return request -> {
                String name = request.getParameter("name");
                String login = request.getParameter("login");
                String email = request.getParameter("email");
                return logic.add(new User(name, login, email)).isPresent();
            };
        }

        private Function<ServletRequest, Boolean> update() {
            return request -> {
                String name = request.getParameter("name");
                String login = request.getParameter("login");
                String email = request.getParameter("email");
                int userId = Integer.parseInt(request.getParameter("userId"));
                return logic.update(userId, new User(userId, name, login, email));
            };
        }

        private Function<ServletRequest, Boolean> delete() {
            return request -> {
                int userId = Integer.parseInt(request.getParameter("userId"));
                return logic.delete(userId);
            };
        }
    }
}