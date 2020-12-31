package ru.job4j.ui.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.AppContext;
import ru.job4j.exception.ConsumerEx;
import ru.job4j.service.ItemsService;
import ru.job4j.service.ValidationService;
import ru.job4j.ui.dto.ErrorResponse;
import ru.job4j.ui.dto.FilterInfo;
import ru.job4j.ui.dto.ItemDto;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.stream.Collectors;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static ru.job4j.util.JsonUtil.extractObject;
import static ru.job4j.util.JsonUtil.sendResponse;

/**
 * ItemsController
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@WebServlet(name = "ItemsController", urlPatterns = "/items")
public class ItemsController extends HttpServlet {

    private final static Logger LOG = LogManager.getLogger(ItemsController.class);

    private final ItemsService itemsService = AppContext.INSTANCE.itemsService;
    private final ValidationService validationService = AppContext.INSTANCE.validationService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        sendResponse(
                itemsService.getActiveItems()
                        .map(ItemDto::fromEntity)
                        .collect(Collectors.toList()),
                resp
        );
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        Optional<FilterInfo> filter = extractObject("filter", req, FilterInfo.class);
        filter.ifPresentOrElse(
                (ConsumerEx<FilterInfo>) filterInfo -> {
                    FilterInfo validFilter = validationService.validateFilterInfo(filterInfo);
                    sendResponse(
                            itemsService.getActiveItems(validFilter)
                                    .map(ItemDto::fromEntity)
                                    .collect(Collectors.toList()),
                            resp
                    );
                },
                () -> sendResponse(new ErrorResponse(SC_BAD_REQUEST, "Invalid filter data"), resp)
        );
    }

}
