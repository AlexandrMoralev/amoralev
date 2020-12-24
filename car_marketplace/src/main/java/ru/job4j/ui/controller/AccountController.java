package ru.job4j.ui.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.AppContext;
import ru.job4j.entity.User;
import ru.job4j.exception.RunnableEx;
import ru.job4j.persistence.impl.UsersDao;
import ru.job4j.service.AuthService;
import ru.job4j.service.ItemsService;
import ru.job4j.service.ValidationService;
import ru.job4j.ui.cache.Cache;
import ru.job4j.ui.dto.ErrorResponse;
import ru.job4j.ui.dto.ItemDto;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static javax.servlet.http.HttpServletResponse.*;
import static ru.job4j.Constants.*;
import static ru.job4j.util.JsonUtil.*;

/**
 * AccountController
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@WebServlet(name = "AccountController", urlPatterns = "/account")
public class AccountController extends HttpServlet {

    private static final Logger LOG = LogManager.getLogger(AccountController.class);

    private final AuthService authService = AppContext.INSTANCE.authService;
    private final ValidationService validationService = AppContext.INSTANCE.validationService;
    private final ItemsService itemsService = AppContext.INSTANCE.itemsService;
    private final UsersDao usersDao = AppContext.INSTANCE.usersDao;
    private final Cache cache = AppContext.INSTANCE.cache;


    private final BiFunction<HttpServletRequest, HttpServletResponse, Consumer<User>> getUserItems = (request, response) ->
            user -> sendResponse(
                    itemsService.getUserItems(user.getId())
                            .stream()
                            .map(ItemDto::fromEntity)
                            .collect(Collectors.toList()),
                    response);


    private final BiFunction<HttpServletRequest, HttpServletResponse, Consumer<User>> addNewItem = (request, response) ->
            user -> extractObject(ITEM, request, ItemDto.class)
                    .ifPresentOrElse(
                            itemDto -> {
                                ItemDto validItem = validationService.validateItem(itemDto);
                                itemsService.saveItem(validItem, user);
                                answerWithStatus(SC_CREATED, response);
                            },
                            () -> sendError(new ErrorResponse(SC_BAD_REQUEST, "Invalid item data"), response)
                    );

    private final BiFunction<HttpServletRequest, HttpServletResponse, Consumer<User>> updateItem = (request, response) ->
            user -> extractObject(ITEM, request, ItemDto.class)
                    .ifPresentOrElse(
                            itemDto -> {
                                ItemDto validItem = validationService.validateItem(itemDto);
                                itemsService.updateItem(validItem);
                                answerWithStatus(SC_OK, response);
                            },
                            () -> sendError(new ErrorResponse(SC_BAD_REQUEST, "Invalid item data"), response)
                    );

    private final BiFunction<HttpServletRequest, HttpServletResponse, Consumer<User>> deleteItem = (request, response) ->
            user -> extractObject(ITEM_ID, request, Integer.class)
                    .ifPresentOrElse(
                            itemId -> {
                                itemsService.getUserItems(user.getId()).stream()
                                        .filter(i -> itemId.equals(i.getId()))
                                        .findFirst()
                                        .ifPresentOrElse(
                                                item -> itemsService.deleteItem(itemId),
                                                () -> sendError(new ErrorResponse(SC_BAD_REQUEST, "Invalid item data"), response)
                                        );

                                answerWithStatus(SC_OK, response);
                            },
                            () -> sendError(new ErrorResponse(SC_BAD_REQUEST, "Invalid itemId"), response)
                    );

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        this.withUser(getUserItems, request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        this.withUser(addNewItem, request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        this.withUser(updateItem, request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        this.withUser(deleteItem, request, response);
    }

    private void withUser(BiFunction<HttpServletRequest, HttpServletResponse, Consumer<User>> action,
                          HttpServletRequest request,
                          HttpServletResponse response
    ) {
        findAuthorizedUserId(request).ifPresentOrElse(
                id -> authService.getUser(id).ifPresentOrElse(
                        action.apply(request, response),
                        () -> sendError(new ErrorResponse(SC_INTERNAL_SERVER_ERROR, "User not found"), response)
                ),
                (RunnableEx) () -> response.sendRedirect(request.getContextPath() + "/index.html")
        );
    }

    private Optional<Integer> findAuthorizedUserId(HttpServletRequest request) {
        return ofNullable(request.getSession(false))
                .map(s -> String.valueOf(s.getAttribute(AUTH_TOKEN)))
                .map(cache::get)
                .filter(v -> v instanceof Integer)
                .map(v -> (Integer) v);
    }

}
