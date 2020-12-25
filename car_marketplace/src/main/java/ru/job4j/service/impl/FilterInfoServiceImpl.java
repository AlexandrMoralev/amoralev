package ru.job4j.service.impl;

import ru.job4j.entity.enumerations.*;
import ru.job4j.service.FilterInfoService;
import ru.job4j.ui.dto.FilterInfo;

import java.util.Collection;

/**
 * FilterInfoServiceImpl
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class FilterInfoServiceImpl implements FilterInfoService {

    private final FilterInfo fullInfo;

    public FilterInfoServiceImpl() {
        this.fullInfo = new FilterInfo(
                this.getItemTypes(),
                this.getMakes(),
                this.getBodyStyles(),
                this.getEngineTypes(),
                this.getDriveTypes(),
                this.getTransmissionTypes(),
                this.getColors()
        );
    }

    @Override
    public Collection<ItemType> getItemTypes() {
        return ItemType.getValues();
    }

    @Override
    public Collection<Make> getMakes() {
        return Make.getValues();
    }

    @Override
    public Collection<BodyStyle> getBodyStyles() {
        return BodyStyle.getValues();
    }

    @Override
    public Collection<EngineType> getEngineTypes() {
        return EngineType.getValues();
    }

    @Override
    public Collection<DriveType> getDriveTypes() {
        return DriveType.getValues();
    }

    @Override
    public Collection<TransmissionType> getTransmissionTypes() {
        return TransmissionType.getValues();
    }

    @Override
    public Collection<Color> getColors() {
        return Color.getValues();
    }

    @Override
    public FilterInfo getFilterInfo() {
        return fullInfo;
    }

}
