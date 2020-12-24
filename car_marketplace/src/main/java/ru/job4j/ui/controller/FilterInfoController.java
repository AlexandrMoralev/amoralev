package ru.job4j.ui.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.AppContext;
import ru.job4j.service.FilterInfoService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ru.job4j.util.JsonUtil.sendResponse;

/**
 * FilterInfoController
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@WebServlet(name = "FilterInfoController", urlPatterns = "/info")
public class FilterInfoController extends HttpServlet {

    private final static Logger LOG = LogManager.getLogger(FilterInfoController.class);

    private final FilterInfoService filterInfoService = AppContext.INSTANCE.filterInfoService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        sendResponse(filterInfoService.getFilterInfo(), resp);
    }

}
