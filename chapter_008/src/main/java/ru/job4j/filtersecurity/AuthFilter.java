package ru.job4j.filtersecurity;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class AuthFilter implements Filter {

    private static final Collection<String> EXCLUSIONS = List.of(
            "/login-view",
            "/create-user",
            "/addresses",
            "/upload"
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        if (req.getSession().getAttribute("principal") != null) {
            req.getServletContext().getRequestDispatcher(req.getRequestURI().replace(req.getContextPath(), "")).forward(req, resp);
            return;
        } else {
            boolean isNeedToLogin = !(isUserCreation(req) || isExclusionPath(req.getRequestURI()));
            if (!isNeedToLogin) {
                chain.doFilter(request, response);
                return;
            } else {
                ServletContext ctx = req.getServletContext();
                RequestDispatcher dispatcher = ctx.getRequestDispatcher("/WEB-INF/views/login-view.jsp");
                dispatcher.forward(request, response);
                return;
            }
        }
    }

    private boolean isUserCreation(HttpServletRequest req) {
        return req.getRequestURI().equals(req.getContextPath() + "/users") && "create".equalsIgnoreCase(req.getParameter("action"));
    }

    private boolean isExclusionPath(String requestURI) {
        return EXCLUSIONS.stream().anyMatch(requestURI::contains);
    }

    @Override
    public void destroy() {
    }
}
