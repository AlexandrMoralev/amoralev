package ru.job4j.ui.dto;

import ru.job4j.entity.enumerations.*;

import java.util.Collection;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

public class FilterInfo {

    private Collection<ItemType> itemTypes;

    private Collection<Make> makes;

    private Collection<BodyStyle> bodyStyles;

    private Collection<EngineType> engineTypes;

    private Collection<DriveType> driveTypes;

    private Collection<TransmissionType> transmissionTypes;

    private Collection<Color> colors;

    private Integer priceMax;

    private Integer mileageMax;

    private Integer hpMin;

    private String producedAfter;

    public FilterInfo() {
    }

    public FilterInfo(
            Collection<ItemType> itemTypes,
            Collection<Make> makes,
            Collection<BodyStyle> bodyStyles,
            Collection<EngineType> engineTypes,
            Collection<DriveType> driveTypes,
            Collection<TransmissionType> transmissionTypes,
            Collection<Color> colors
    ) {
        this.itemTypes = itemTypes;
        this.makes = makes;
        this.bodyStyles = bodyStyles;
        this.engineTypes = engineTypes;
        this.driveTypes = driveTypes;
        this.transmissionTypes = transmissionTypes;
        this.colors = colors;
    }

    public Collection<ItemType> getItemTypes() {
        return itemTypes;
    }

    public void setItemTypes(Collection<ItemType> itemTypes) {
        this.itemTypes = itemTypes;
    }

    public Collection<Make> getMakes() {
        return makes;
    }

    public void setMakes(Collection<Make> makes) {
        this.makes = makes;
    }

    public Collection<BodyStyle> getBodyStyles() {
        return bodyStyles;
    }

    public void setBodyStyles(Collection<BodyStyle> bodyStyles) {
        this.bodyStyles = bodyStyles;
    }

    public Collection<EngineType> getEngineTypes() {
        return engineTypes;
    }

    public void setEngineTypes(Collection<EngineType> engineTypes) {
        this.engineTypes = engineTypes;
    }

    public Collection<DriveType> getDriveTypes() {
        return driveTypes;
    }

    public void setDriveTypes(Collection<DriveType> driveTypes) {
        this.driveTypes = driveTypes;
    }

    public Collection<TransmissionType> getTransmissionTypes() {
        return transmissionTypes;
    }

    public void setTransmissionTypes(Collection<TransmissionType> transmissionTypes) {
        this.transmissionTypes = transmissionTypes;
    }

    public Collection<Color> getColors() {
        return colors;
    }

    public void setColors(Collection<Color> colors) {
        this.colors = colors;
    }

    public Optional<Integer> getPriceMax() {
        return ofNullable(priceMax);
    }

    public void setPriceMax(Integer priceMax) {
        this.priceMax = priceMax;
    }

    public Optional<Integer> getMileageMax() {
        return ofNullable(mileageMax);
    }

    public void setMileageMax(Integer mileageMax) {
        this.mileageMax = mileageMax;
    }

    public Optional<String> getProducedAfter() {
        return ofNullable(producedAfter);
    }

    public void setProducedAfter(String producedAfter) {
        this.producedAfter = producedAfter;
    }

    public Optional<Integer> getHpMin() {
        return ofNullable(hpMin);
    }

    public void setHpMin(Integer hpMin) {
        this.hpMin = hpMin;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", FilterInfo.class.getSimpleName() + "[", "]")
                .add("itemTypes=" + join(itemTypes))
                .add("makes=" + join(makes))
                .add("bodyStyles=" + join(bodyStyles))
                .add("engineTypes=" + join(engineTypes))
                .add("driveTypes=" + join(driveTypes))
                .add("transmissionTypes=" + join(transmissionTypes))
                .add("colors=" + join(colors))
                .add("priceMax" + ofNullable(priceMax).map(String::valueOf).orElse(""))
                .add("mileageMax" + ofNullable(mileageMax).map(String::valueOf).orElse(""))
                .add("producedAfter" + ofNullable(producedAfter).orElse(""))
                .add("hpMin" + ofNullable(hpMin).map(String::valueOf).orElse(""))
                .toString();
    }

    private <T extends Enum> String join(Collection<T> enumCollection) {
        return enumCollection.stream().map(Enum::name).collect(Collectors.joining(","));
    }
}
