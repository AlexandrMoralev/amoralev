package ru.job4j.ui.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.ui.dto.ErrorResponse;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static ru.job4j.util.JsonUtil.sendError;

/**
 * ErrorsHandlingFilter
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@WebFilter(urlPatterns = "/*")
public class ErrorsHandlingFilter implements Filter {

    private final static Logger LOG = LogManager.getLogger(ErrorsHandlingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        try {
            chain.doFilter(request, response);
        } catch (RuntimeException e) {
            LOG.error(e);
        } catch (Throwable t) {
            LOG.error(t);
            sendError(new ErrorResponse(SC_INTERNAL_SERVER_ERROR, "Unexpected error."), response);
        }

    }

}
