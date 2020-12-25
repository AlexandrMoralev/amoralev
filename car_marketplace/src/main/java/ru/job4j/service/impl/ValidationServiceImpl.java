package ru.job4j.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.exception.ValidationException;
import ru.job4j.service.FilterInfoService;
import ru.job4j.service.ValidationService;
import ru.job4j.ui.dto.AuthRequest;
import ru.job4j.ui.dto.FilterInfo;
import ru.job4j.ui.dto.ItemDto;

import java.util.Collection;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static ru.job4j.ui.dto.AuthAction.UNKNOWN;

/**
 * ValidationServiceImpl
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class ValidationServiceImpl implements ValidationService {

    private static final Logger LOG = LogManager.getLogger(ValidationServiceImpl.class);

    private final Pattern phonePattern = Pattern.compile("\\d{3}-\\d{7}");

    private final FilterInfoService filterInfoService;

    public ValidationServiceImpl(
            FilterInfoService filterInfoService
    ) {
        this.filterInfoService = filterInfoService;
    }

    @Override
    public AuthRequest validateUser(AuthRequest user) throws ValidationException {
        if (user.getAction() == UNKNOWN) {
            throw new ValidationException("Invalid action");
        }
        if (Stream.of(user.getPassword(), user.getPhone())
                .map(String::strip)
                .anyMatch(String::isBlank)) {
            throw new ValidationException("Invalid user credentials");
        }
        if (!phonePattern.matcher(user.getPhone()).matches()) {
            throw new ValidationException("Invalid phone");
        }
        return user;
    }

    @Override
    public ItemDto validateItem(ItemDto item) throws ValidationException {
        if (item.getCar() == null) {
            throw new ValidationException("Invalid item product");
        }
        if (item.getType() == null) {
            throw new ValidationException("Invalid item type");
        }
        if (item.getActive() == null) {
            throw new ValidationException("Invalid item active");
        }
        if (item.getPhotoIds() == null) {
            throw new ValidationException("Invalid item photos");
        }
        return item;
    }

    @Override
    public FilterInfo validateFilterInfo(FilterInfo filter) throws ValidationException {
        FilterInfo filterInfo = this.filterInfoService.getFilterInfo();
        check(filterInfo.getBodyStyles(), filter.getBodyStyles());
        check(filterInfo.getColors(), filter.getColors());
        check(filterInfo.getDriveTypes(), filter.getDriveTypes());
        check(filterInfo.getEngineTypes(), filter.getEngineTypes());
        check(filterInfo.getItemTypes(), filter.getItemTypes());
        check(filterInfo.getTransmissionTypes(), filter.getTransmissionTypes());
        check(filterInfo.getMakes(), filter.getMakes());
        return filter;
    }

    private <T> void check(Collection<T> base, Collection<T> filter) {
        if (!base.containsAll(filter)) {
            throw new ValidationException(base.stream().findAny().map(v -> v.getClass().getSimpleName()).orElse("") + " invalid filter");
        }
    }

}
