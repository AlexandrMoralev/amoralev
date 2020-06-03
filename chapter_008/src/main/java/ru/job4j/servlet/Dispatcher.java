package ru.job4j.servlet;

import ru.job4j.crudservlet.Address;
import ru.job4j.crudservlet.User;
import ru.job4j.crudservlet.ValidationService;
import ru.job4j.filtersecurity.Role;
import ru.job4j.uploaddownloadfiles.AttachmentService;
import ru.job4j.uploaddownloadfiles.PhotoService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.function.Function;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static java.util.function.Predicate.not;

/**
 * Dispatcher
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public enum Dispatcher {
    INSTANCE;
    private final Map<String, Function<HttpServletRequest, Boolean>> dispatcher;
    private final Validation<User> logic;
    private final AttachmentService attachmentService;

    Dispatcher() {
        this.dispatcher = Map.of(
                "create", create(),
                "update", update(),
                "delete", delete(),
                "noAction", noAction()
        );
        this.logic = ValidationService.INSTANCE;
        this.attachmentService = PhotoService.INSTANCE;
    }

    public boolean execute(HttpServletRequest req) {
        Function<HttpServletRequest, Boolean> func = dispatcher.get(req.getParameter("action"));
        return func != null ? func.apply(req) : false;
    }

    public Function<HttpServletRequest, Boolean> create() {
        return request ->
                logic.add(User.newBuilder()
                        .setName(request.getParameter("name"))
                        .setLogin(request.getParameter("login"))
                        .setPassword(request.getParameter("password"))
                        .setRole(Role.valueOf(request.getParameter("role").toUpperCase()))
                        .setAddress(Address.newBuilder()
                                .setCountry(request.getParameter("country"))
                                .setCity(request.getParameter("city"))
                                .build())
                        //  TODO when switching to some modern ui framework use attachmentService.createAttachment(request)
                        .setPhotoId(request.getParameter("photoId"))
                        .build()
                ).isPresent();
    }

    public Function<HttpServletRequest, Boolean> update() {
        return request -> {
            User.Builder user = User.newBuilder();
            of(request.getParameter("userId")).map(Integer::valueOf).ifPresent(user::setId);
            ofNullable(request.getParameter("name")).ifPresent(user::setName);
            ofNullable(request.getParameter("login")).ifPresent(user::setLogin);
            ofNullable(request.getParameter("role")).map(String::toUpperCase).map(Role::valueOf).ifPresent(user::setRole);
            of(request.getParameter("addressId")).map(Integer::valueOf)
                    .ifPresent(
                            id -> {
                                Address.Builder address = Address.newBuilder();
                                address.setId(id);
                                of(request.getParameter("country")).ifPresent(address::setCountry);
                                of(request.getParameter("city")).ifPresent(address::setCity);
                                user.setAddress(address.build());
                            }
                    );
            //  TODO when switching to some modern ui framework use attachmentService.updateAttachment(request)
            ofNullable(request.getParameter("photoId")).ifPresent(user::setPhotoId);
            return logic.update(user.build());
        };
    }

    public Function<HttpServletRequest, Boolean> delete() {
        return request -> {
            ofNullable(request.getParameter("userId"))
                    .map(Integer::valueOf)
                    .flatMap(logic::findById)
                    .ifPresent(user -> {
                        ofNullable(user.getPhotoId()).filter(not(String::isBlank)).ifPresent(attachmentService::deleteAttachment);
                        logic.delete(user.getId());
                    });
            return true;
        };
    }

    public Function<HttpServletRequest, Boolean> noAction() {
        return request -> true;
    }
}

