package ru.job4j.filtersecurity;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthFilter implements Filter {

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
            boolean isNeedToLogin = !(req.getRequestURI().contains("/login-view")
                    || req.getRequestURI().contains("/create-user")
                    || req.getRequestURI().contains("/addresses")
                    || (req.getRequestURI().equals(req.getContextPath() + "/users") && "create".equalsIgnoreCase(req.getParameter("action"))));
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

    @Override
    public void destroy() {
    }
}
