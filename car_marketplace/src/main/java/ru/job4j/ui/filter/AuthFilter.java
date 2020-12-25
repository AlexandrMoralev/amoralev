package ru.job4j.ui.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static java.util.Optional.ofNullable;
import static java.util.function.Predicate.not;
import static ru.job4j.Constants.AUTH_TOKEN;

/**
 * AuthFilter
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@WebFilter(urlPatterns = "/*")
public class AuthFilter implements Filter {

    private final List<String> whiteList = List.of(
            "/info",
            "/items",
            "/file",
            "/index.html"
    );

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        HttpSession session = request.getSession(false);

        boolean isLoggedIn = ofNullable(session).map(s -> isAttributeExists(AUTH_TOKEN, session)).orElse(false);
        String requestURI = request.getRequestURI().trim();
        boolean isLoginRequest = requestURI.contains("/login") || requestURI.contains("/auth");
        boolean isWhiteListPath = whiteList.stream()
                .anyMatch(requestURI::contains);

        if (isLoggedIn || isLoginRequest || isWhiteListPath) {
            chain.doFilter(req, resp);
        } else {
            response.sendRedirect(request.getContextPath() + "/auth-page.html");
        }
    }

    private boolean isAttributeExists(String name, HttpSession session) {
        return ofNullable(session.getAttribute(name))
                .map(String::valueOf)
                .map(String::strip)
                .filter(not(String::isBlank))
                .isPresent();
    }
}
