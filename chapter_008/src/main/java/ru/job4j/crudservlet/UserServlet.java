package ru.job4j.crudservlet;

import net.jcip.annotations.ThreadSafe;
import ru.job4j.filtersecurity.Role;

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

import static java.util.Optional.ofNullable;

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

    public UserServlet() {
        this.logic = ValidationService.INSTANCE;
        this.dispatcher = new Dispatcher();
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
            return request -> logic.add(extractUser.apply(request)).isPresent();
        }

        private Function<ServletRequest, Boolean> update() {
            return request -> logic.update(extractUser.apply(request));
        }

        private Function<ServletRequest, Boolean> delete() {
            return request -> logic.delete(Integer.parseInt(request.getParameter("userId")));
        }

        private Function<ServletRequest, User> extractUser = request -> {
            User.Builder user = new User.Builder();
            ofNullable(request.getParameter("userId")).map(Integer::parseInt).ifPresent(user::setId);
            ofNullable(request.getParameter("name")).map(String::strip).ifPresent(user::setName);
            ofNullable(request.getParameter("login")).map(String::strip).ifPresent(user::setLogin);
            ofNullable(request.getParameter("email")).map(String::strip).ifPresent(user::setEmail);
            ofNullable(request.getParameter("password")).map(String::strip).ifPresent(user::setPassword);
            ofNullable(request.getParameter("role")).map(String::strip).map(Role::valueOf).ifPresent(user::setRole);
            return user.build();
        };
    }
}