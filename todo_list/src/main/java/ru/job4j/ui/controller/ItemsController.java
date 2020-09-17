package ru.job4j.ui.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.AppContext;
import ru.job4j.domain.User;
import ru.job4j.exception.RunnableEx;
import ru.job4j.service.ValidationService;
import ru.job4j.ui.cache.Cache;
import ru.job4j.ui.model.ErrorResponse;
import ru.job4j.ui.model.ItemDTO;
import ru.job4j.ui.model.ItemsDTO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import static java.util.Optional.ofNullable;
import static javax.servlet.http.HttpServletResponse.*;
import static ru.job4j.util.Constants.AUTH_TOKEN;
import static ru.job4j.util.Constants.ITEM;
import static ru.job4j.util.JsonUtil.*;

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

    private final ValidationService validationService = AppContext.INSTANCE.validationService;
    private final Cache cache = AppContext.INSTANCE.cache;

    private final BiFunction<HttpServletRequest, HttpServletResponse, Consumer<User>> getUserTasks = (request, response) ->
            user -> sendResponse(new ItemsDTO(user.getName(), validationService.getUserTasks(user.getId())), response);


    private final BiFunction<HttpServletRequest, HttpServletResponse, Consumer<User>> addNewItem = (request, response) ->
            user -> extractObject(ITEM, request, ItemDTO.class)
                    .ifPresentOrElse(
                            item -> {
                                validationService.addItem(item.toModel(), user);
                                answerWithStatus(SC_CREATED, response);
                            },
                            () -> sendError(new ErrorResponse(SC_BAD_REQUEST, "Invalid item data"), response)
                    );

    private final BiFunction<HttpServletRequest, HttpServletResponse, Consumer<User>> updateItem = (request, response) ->
            user -> extractObject(ITEM, request, ItemDTO.class)
                    .ifPresentOrElse(
                            item -> {
                                validationService.updateItem(item.toModel(), user);
                                answerWithStatus(SC_OK, response);
                            },
                            () -> sendError(new ErrorResponse(SC_BAD_REQUEST, "Invalid item data"), response)
                    );

    private final BiFunction<HttpServletRequest, HttpServletResponse, Consumer<User>> deleteItem = (request, response) ->
            user -> extractObject(ITEM, request, ItemDTO.class)
                    .ifPresentOrElse(
                            item -> {
                                validationService.deleteItem(item.getId(), user);
                                answerWithStatus(SC_OK, response);
                            },
                            () -> sendError(new ErrorResponse(SC_BAD_REQUEST, "Invalid item id"), response)
                    );

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        this.withUser(getUserTasks, req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        this.withUser(addNewItem, req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        this.withUser(updateItem, req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        this.withUser(deleteItem, req, resp);
    }

    private void withUser(BiFunction<HttpServletRequest, HttpServletResponse, Consumer<User>> action,
                          HttpServletRequest req,
                          HttpServletResponse resp
    ) {
        findAuthorizedUserId(req).ifPresentOrElse(
                id -> validationService.getUser(id).ifPresentOrElse(
                        action.apply(req, resp),
                        () -> sendError(new ErrorResponse(SC_INTERNAL_SERVER_ERROR, "User not found"), resp)
                ),
                (RunnableEx) () -> resp.sendRedirect(req.getContextPath() + "/")
        );
    }

    private Optional<Integer> findAuthorizedUserId(HttpServletRequest req) {
        return ofNullable(req.getSession(false))
                .map(s -> String.valueOf(s.getAttribute(AUTH_TOKEN)))
                .map(cache::get)
                .filter(v -> v instanceof Integer)
                .map(v -> (Integer) v);
    }

}
