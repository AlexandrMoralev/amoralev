package ru.job4j.service;

import ru.job4j.entity.enumerations.*;
import ru.job4j.ui.dto.FilterInfo;

import java.util.Collection;

/**
 * FilterInfoService
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface FilterInfoService {

    Collection<ItemType> getItemTypes();

    Collection<Make> getMakes();

    Collection<BodyStyle> getBodyStyles();

    Collection<EngineType> getEngineTypes();

    Collection<DriveType> getDriveTypes();

    Collection<TransmissionType> getTransmissionTypes();

    Collection<Color> getColors();

    FilterInfo getFilterInfo();

}
