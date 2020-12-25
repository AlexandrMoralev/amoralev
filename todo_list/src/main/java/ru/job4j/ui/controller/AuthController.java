package ru.job4j.ui.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.AppContext;
import ru.job4j.domain.User;
import ru.job4j.exception.CommonException;
import ru.job4j.exception.ConsumerEx;
import ru.job4j.exception.RunnableEx;
import ru.job4j.security.SecurityService;
import ru.job4j.service.ValidationService;
import ru.job4j.ui.cache.Cache;
import ru.job4j.ui.model.AuthAction;
import ru.job4j.ui.model.AuthRequest;
import ru.job4j.ui.model.ErrorResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import static java.util.Optional.ofNullable;
import static javax.servlet.http.HttpServletResponse.*;
import static ru.job4j.util.Constants.AUTH_REQUEST;
import static ru.job4j.util.Constants.AUTH_TOKEN;
import static ru.job4j.util.JsonUtil.*;

/**
 * AuthController
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@WebServlet(name = "AuthController", urlPatterns = "/login")
public class AuthController extends HttpServlet {

    private static final Logger LOG = LogManager.getLogger(AuthController.class);

    private final ValidationService validationService = AppContext.INSTANCE.validationService;
    private final SecurityService securityService = AppContext.INSTANCE.securityService;
    private final Cache cache = AppContext.INSTANCE.cache;

    private final int defaultExpirationPeriod = AppContext.INSTANCE.config.getInt("cache.expiration.period.seconds") * 1000;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("/index.html");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        AuthRequest auth = getAuthRequest(req, resp);
        AuthAction action = auth.getAction();

        final RunnableEx none = () -> {
        };

        final RunnableEx signInWithNewSession = () -> signIn.apply(req, resp).accept(req.getSession(true));

        switch (action) {
            case SIGNIN:
                withSession(signIn, signInWithNewSession, req, resp);
                return;
            case LOGOUT:
                withSession(invalidateSession, none, req, resp);
                redirectTo("/index.html", req, resp);
                return;
            case REGISTER:
                withSession(invalidateSession, none, req, resp);
                registerNewUser(req, resp);
                return;
            case DEFAULT:
            default:
                sendError(new ErrorResponse(SC_BAD_REQUEST, "Invalid request."), resp);
        }
    }

    private AuthRequest getAuthRequest(HttpServletRequest req, HttpServletResponse resp) {
        Optional<AuthRequest> authRequest = extractObject(AUTH_REQUEST, req, AuthRequest.class).filter(AuthRequest::isValid);
        if (authRequest.isEmpty()) {
            sendError(new ErrorResponse(SC_BAD_REQUEST, "Invalid auth data"), resp);
        }
        AuthRequest auth = authRequest.orElseThrow(() -> new CommonException("Server error"));
        req.setAttribute(AUTH_REQUEST, auth);
        return auth;
    }

    private void withSession(BiFunction<HttpServletRequest, HttpServletResponse, Consumer<HttpSession>> ifSome,
                             RunnableEx ifNone,
                             HttpServletRequest req,
                             HttpServletResponse resp
    ) {
        ofNullable(req.getSession(false))
                .ifPresentOrElse(
                        ifSome.apply(req, resp),
                        ifNone
                );
    }

    private final BiFunction<HttpServletRequest, HttpServletResponse, Consumer<HttpSession>> invalidateSession = (request, response) ->
            session -> {
                ofNullable(session.getAttribute(AUTH_TOKEN))
                        .map(String::valueOf)
                        .ifPresent(cache::remove);
                session.invalidate();
            };

    private final BiFunction<HttpServletRequest, HttpServletResponse, Consumer<HttpSession>> signIn = (request, response) ->
            session -> ofNullable(session.getAttribute(AUTH_TOKEN))
                    .map(String::valueOf)
                    .ifPresentOrElse(
                            authToken -> {
                                Integer userId = (Integer) cache.get(authToken);
                                signInUser(validationService.getUser(userId), request, response);
                            },
                            () -> signInUser(validationService.findUserByLogin(getAuth(request).getLogin()), request, response)
                    );

    private void signInUser(Optional<User> userOptional, HttpServletRequest req, HttpServletResponse resp) {
        AuthRequest auth = getAuth(req);
        userOptional
                .ifPresentOrElse(
                        (ConsumerEx<User>) user -> {
                            if (validationService.isCredential(auth.getLogin(), auth.getPassword())) {
                                createNewSession(req, user);
                                answerWithStatus(SC_OK, resp);
                            } else {
                                sendError(new ErrorResponse(SC_UNAUTHORIZED, "Invalid credentials."), resp);
                            }
                        },
                        (RunnableEx) () -> sendError(new ErrorResponse(SC_UNAUTHORIZED, "User not found."), resp)
                );
    }

    private void registerNewUser(HttpServletRequest request, HttpServletResponse response) {
        AuthRequest auth = getAuth(request);
        validationService.findUserByLogin(auth.getLogin())
                .ifPresentOrElse(
                        u -> sendError(new ErrorResponse(SC_FORBIDDEN, "User already registered."), response),
                        (RunnableEx) () -> {
                            User registeredUser = validationService.addUser(auth.getLogin(), auth.getPassword());
                            createNewSession(request, registeredUser);
                            answerWithStatus(SC_CREATED, response);
                        }
                );
    }

    private HttpSession createNewSession(HttpServletRequest req, User user) {
        HttpSession session = req.getSession(true);
        String token = securityService.generateToken();
        cache.add(token, user.getId(), defaultExpirationPeriod);
        session.setAttribute(AUTH_TOKEN, token);
        return session;
    }

    private AuthRequest getAuth(HttpServletRequest request) {
        return ofNullable(request.getAttribute(AUTH_REQUEST))
                .map(v -> (AuthRequest) v)
                .filter(AuthRequest::isValid)
                .orElseThrow(() -> {
                    LOG.error("Auth not found");
                    return new CommonException("Server error");
                });
    }

    private void redirectTo(String path, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(req.getContextPath() + path);
    }

}
