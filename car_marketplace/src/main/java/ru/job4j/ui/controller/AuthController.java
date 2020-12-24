package ru.job4j.ui.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.AppContext;
import ru.job4j.entity.User;
import ru.job4j.exception.CommonException;
import ru.job4j.exception.ConsumerEx;
import ru.job4j.exception.RunnableEx;
import ru.job4j.service.AuthService;
import ru.job4j.service.SecurityService;
import ru.job4j.service.ValidationService;
import ru.job4j.ui.cache.Cache;
import ru.job4j.ui.dto.AuthAction;
import ru.job4j.ui.dto.AuthRequest;
import ru.job4j.ui.dto.ErrorResponse;

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
import static ru.job4j.Constants.AUTH_REQUEST;
import static ru.job4j.Constants.AUTH_TOKEN;
import static ru.job4j.util.JsonUtil.*;

/**
 * AuthController
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@WebServlet(name = "AuthController", urlPatterns = "/auth")
public class AuthController extends HttpServlet {

    private static final Logger LOG = LogManager.getLogger(AuthController.class);

    private final ValidationService validationService = AppContext.INSTANCE.validationService;
    private final AuthService authService = AppContext.INSTANCE.authService;
    private final SecurityService securityService = AppContext.INSTANCE.securityService;
    private final Cache cache = AppContext.INSTANCE.cache;

    private final long defaultExpirationPeriodMillis = AppContext.INSTANCE.config.getInt("cache.expiration.period.seconds") * 1000;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("/auth-page.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        AuthRequest auth = getAuthRequest(request, response);
        AuthAction action = auth.getAction();

        final RunnableEx none = () -> {
        };

        final RunnableEx signInWithNewSession = () -> signIn.apply(request, response).accept(request.getSession(true));

        switch (action) {
            case SIGNIN:
                withSession(signIn, signInWithNewSession, request, response);
                return;
            case SIGNUP:
                withSession(invalidateSession, none, request, response);
                registerNewUser(request, response);
                return;
            case UNKNOWN:
            default:
                sendError(new ErrorResponse(SC_BAD_REQUEST, "Invalid request."), response);
        }
    }

    private AuthRequest getAuthRequest(HttpServletRequest request, HttpServletResponse response) {
        Optional<AuthRequest> authRequest = extractObject(AUTH_REQUEST, request, AuthRequest.class).filter(AuthRequest::isValid);
        if (authRequest.isEmpty()) {
            sendError(new ErrorResponse(SC_BAD_REQUEST, "Invalid auth data"), response);
        }
        AuthRequest auth = authRequest.orElseThrow(() -> new CommonException("Server error"));
        request.setAttribute(AUTH_REQUEST, auth);
        return auth;
    }

    private void withSession(BiFunction<HttpServletRequest, HttpServletResponse, Consumer<HttpSession>> ifSome,
                             RunnableEx ifNone,
                             HttpServletRequest request,
                             HttpServletResponse response
    ) {
        ofNullable(request.getSession(false))
                .ifPresentOrElse(
                        ifSome.apply(request, response),
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
                                ofNullable(cache.get(authToken))
                                        .map(v -> (Integer) v)
                                        .ifPresentOrElse(
                                                userId -> signInUser(authService.getUser(userId), request, response),
                                                () -> signInUser(authService.findUserByPhone(getAuth(request).getPhone()), request, response)
                                        );
                            },
                            () -> signInUser(authService.findUserByPhone(getAuth(request).getPhone()), request, response)
                    );

    private void signInUser(Optional<User> userOptional, HttpServletRequest request, HttpServletResponse response) {
        AuthRequest auth = getAuth(request);
        userOptional
                .ifPresentOrElse(
                        (ConsumerEx<User>) user -> {
                            if (authService.isCredential(auth.getPhone(), auth.getPassword())) {
                                createNewSession(request, user);
                                answerWithStatus(SC_OK, response);
                            } else {
                                sendError(new ErrorResponse(SC_UNAUTHORIZED, "Invalid credentials."), response);
                            }
                        },
                        (RunnableEx) () -> sendError(new ErrorResponse(SC_UNAUTHORIZED, "User not found."), response)
                );
    }

    private void registerNewUser(HttpServletRequest request, HttpServletResponse response) {
        AuthRequest auth = getAuth(request);
        authService.findUserByPhone(auth.getPhone())
                .ifPresentOrElse(
                        u -> sendError(new ErrorResponse(SC_FORBIDDEN, "User already registered."), response),
                        (RunnableEx) () -> {
                            User registeredUser = authService.addUser(auth.getPhone(), auth.getPassword());
                            createNewSession(request, registeredUser);
                            answerWithStatus(SC_CREATED, response);
                        }
                );
    }

    private HttpSession createNewSession(HttpServletRequest request, User user) {
        HttpSession session = request.getSession(true);
        String token = securityService.generateToken();
        cache.add(token, user.getId(), defaultExpirationPeriodMillis);
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

}
