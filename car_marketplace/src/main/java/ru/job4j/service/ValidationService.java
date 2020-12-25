package ru.job4j.service;

import ru.job4j.exception.ValidationException;
import ru.job4j.ui.dto.AuthRequest;
import ru.job4j.ui.dto.FilterInfo;
import ru.job4j.ui.dto.ItemDto;

/**
 * ValidationService
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface ValidationService {

    AuthRequest validateUser(AuthRequest user) throws ValidationException;

    ItemDto validateItem(ItemDto item) throws ValidationException;

    FilterInfo validateFilterInfo(FilterInfo info) throws ValidationException;

}
