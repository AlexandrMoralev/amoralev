package ru.job4j.persistence;

import ru.job4j.entity.*;
import ru.job4j.ui.dto.FilterInfo;
import ru.job4j.util.DateTimeUtils;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * FilterInfoResolver - converts <code>FilterInfo</code>
 *      to <code>Stream</code> of <code>javax.persistence.criteria.Predicate</code>
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public final class FilterInfoResolver {

    private final CriteriaBuilder criteriaBuilder;
    private final Root<Item> itemRoot;
    private final FilterInfo filterInfo;

    private FilterInfoResolver(CriteriaBuilder criteriaBuilder,
                               Root<Item> itemRoot,
                               FilterInfo filterInfo
    ) {
        this.criteriaBuilder = criteriaBuilder;
        this.itemRoot = itemRoot;
        this.filterInfo = filterInfo;
    }

    public static Optional<FilterInfoResolver> of(CriteriaBuilder criteriaBuilder,
                                                  Root<Item> itemRoot,
                                                  FilterInfo filterInfo
    ) {
        return Optional.of(new FilterInfoResolver(criteriaBuilder, itemRoot, filterInfo));
    }

    public Stream<Predicate> createActiveItemsPredicates() {
        Stream<Predicate> predicates = this.createItemsPredicates();
        return Stream.concat(
                Stream.of(activeItemPredicate(criteriaBuilder, itemRoot, true)),
                predicates
        );
    }

    public Stream<Predicate> createItemsPredicates() {
        return Stream.of(
                createItemRootPredicates(),
                createCarRootPredicates(),
                createProductionInfoRootPredicates(),
                createProductionInfoRootPredicates(),
                createEngineRootPredicates(),
                createTransmissionRootPredicates()
        ).flatMap(Function.identity());
    }


    private Stream<Predicate> createItemRootPredicates() {
        return Stream.of(
                getItemCreationDatePredicate(),
                getItemHasPhotosPredicate(),
                getItemTypesPredicate()
        ).flatMap(Function.identity());
    }

    private Stream<Predicate> createCarRootPredicates() {
        return Stream.of(
                getCarPricePredicate(),
                getCarMileagePredicate(),
                getCarBodyStylesPredicate()
        ).flatMap(Function.identity());
    }

    private Stream<Predicate> createProductionInfoRootPredicates() {
        return Stream.of(
                getProductProducedDatePredicate(),
                getProductMakesPredicate(),
                getProductDriveTypesPredicate(),
                getProductColorsPredicate()
        ).flatMap(Function.identity());
    }

    private Stream<Predicate> createEngineRootPredicates() {
        return Stream.of(
                getEnginePowerPredicate(),
                getEngineTypesPredicate()
        ).flatMap(Function.identity());
    }

    private Stream<Predicate> createTransmissionRootPredicates() {
        return Stream.of(
                getTransmissionTypesPredicate()
        ).flatMap(Function.identity());
    }


    /// item root predicates
    private Stream<Predicate> getItemCreationDatePredicate() {
        return filterInfo.getItemCreatedAfter()
                .map(DateTimeUtils::convertToLocalDateTime)
                .map(createdAfter -> itemCreationDatePredicate(criteriaBuilder, itemRoot, createdAfter))
                .stream();
    }

    private Stream<Predicate> getItemHasPhotosPredicate() {
        return filterInfo.getHasPhoto()
                .filter(v -> v)
                .map(needOnlyWithPhoto -> itemHasPhotosPredicate(criteriaBuilder, itemRoot, true))
                .stream();
    }

    private Stream<Predicate> getItemTypesPredicate() {
        return filterInfo.getItemTypes().isEmpty()
                ? Stream.empty()
                : Stream.of(itemTypesPredicate(criteriaBuilder, itemRoot, filterInfo));
    }

    // car predicates
    private Stream<Predicate> getCarPricePredicate() {
        return filterInfo.getPriceMax()
                .map(priceMax -> carPricePredicate(criteriaBuilder, itemRoot, priceMax))
                .stream();
    }

    private Stream<Predicate> getCarMileagePredicate() {
        return filterInfo.getMileageMax()
                .map(mileageMax -> carMileagePredicate(criteriaBuilder, itemRoot, mileageMax))
                .stream();
    }

    private Stream<Predicate> getCarBodyStylesPredicate() {
        return filterInfo.getBodyStyles().isEmpty()
                ? Stream.empty()
                : Stream.of(carBodyStylesPredicate(criteriaBuilder, itemRoot, filterInfo));
    }

    // productionInfo predicates
    private Stream<Predicate> getProductProducedDatePredicate() {
        return filterInfo.getProductProducedAfter()
                .map(DateTimeUtils::convertToLocalDateTime)
                .map(producedAfter -> carProducedDatePredicate(criteriaBuilder, itemRoot, producedAfter))
                .stream();
    }

    private Stream<Predicate> getProductMakesPredicate() {
        return filterInfo.getMakes().isEmpty()
                ? Stream.empty()
                : Stream.of(carMakesPredicate(criteriaBuilder, itemRoot, filterInfo));
    }

    private Stream<Predicate> getProductDriveTypesPredicate() {
        return filterInfo.getDriveTypes().isEmpty()
                ? Stream.empty()
                : Stream.of(carDriveTypesPredicate(criteriaBuilder, itemRoot, filterInfo));
    }

    private Stream<Predicate> getProductColorsPredicate() {
        return filterInfo.getColors().isEmpty()
                ? Stream.empty()
                : Stream.of(carColorsPredicate(criteriaBuilder, itemRoot, filterInfo));
    }


    // engine predicates
    private Stream<Predicate> getEnginePowerPredicate() {
        return filterInfo.getHpMin()
                .map(hpMin -> enginePowerPredicate(criteriaBuilder, itemRoot, hpMin))
                .stream();
    }

    private Stream<Predicate> getEngineTypesPredicate() {
        return filterInfo.getEngineTypes().isEmpty()
                ? Stream.empty()
                : Stream.of(engineTypesPredicate(criteriaBuilder, itemRoot, filterInfo));
    }

    // transmission predicates
    private Stream<Predicate> getTransmissionTypesPredicate() {
        return filterInfo.getTransmissionTypes().isEmpty()
                ? Stream.empty()
                : Stream.of(transmissionTypesPredicate(criteriaBuilder, itemRoot, filterInfo));
    }


    private static Predicate transmissionTypesPredicate(CriteriaBuilder criteriaBuilder,
                                                 Root<Item> itemRoot,
                                                 FilterInfo filterInfo
    ) {
        return criteriaBuilder.isTrue(
                itemRoot.get(Item_.car)
                        .get(Car_.productionInfo)
                        .get(ProductionInfo_.transmission)
                        .get(Transmission_.type)
                        .in(filterInfo.getTransmissionTypes())
        );
    }

    private static Predicate engineTypesPredicate(CriteriaBuilder criteriaBuilder,
                                           Root<Item> itemRoot,
                                           FilterInfo filterInfo
    ) {
        return criteriaBuilder.isTrue(
                itemRoot.get(Item_.car)
                        .get(Car_.productionInfo)
                        .get(ProductionInfo_.engine)
                        .get(Engine_.type)
                        .in(filterInfo.getEngineTypes())
        );
    }

    private static Predicate enginePowerPredicate(CriteriaBuilder criteriaBuilder,
                                           Root<Item> itemRoot,
                                           Integer hp
    ) {
        return criteriaBuilder.ge(itemRoot.get(Item_.car)
                .get(Car_.productionInfo)
                .get(ProductionInfo_.engine)
                .get(Engine_.hp), hp);
    }

    private static Predicate carColorsPredicate(CriteriaBuilder criteriaBuilder,
                                         Root<Item> itemRoot,
                                         FilterInfo filterInfo
    ) {
        return criteriaBuilder.isTrue(
                itemRoot.get(Item_.car)
                        .get(Car_.productionInfo)
                        .get(ProductionInfo_.color)
                        .in(filterInfo.getColors())
        );
    }

    private static Predicate carDriveTypesPredicate(CriteriaBuilder criteriaBuilder,
                                             Root<Item> itemRoot,
                                             FilterInfo filterInfo
    ) {
        return criteriaBuilder.isTrue(
                itemRoot.get(Item_.car)
                        .get(Car_.productionInfo)
                        .get(ProductionInfo_.driveType)
                        .in(filterInfo.getDriveTypes())
        );
    }

    private static Predicate carMakesPredicate(CriteriaBuilder criteriaBuilder,
                                        Root<Item> itemRoot,
                                        FilterInfo filterInfo
    ) {
        return criteriaBuilder.isTrue(
                itemRoot.get(Item_.car)
                        .get(Car_.productionInfo)
                        .get(ProductionInfo_.make)
                        .in(filterInfo.getMakes())
        );
    }

    private static Predicate carProducedDatePredicate(CriteriaBuilder criteriaBuilder,
                                               Root<Item> itemRoot,
                                               LocalDateTime producedAfter
    ) {
        return criteriaBuilder.greaterThanOrEqualTo(
                itemRoot.get(Item_.car)
                        .get(Car_.productionInfo)
                        .get(ProductionInfo_.producedAt),
                producedAfter);
    }

    private static Predicate carBodyStylesPredicate(CriteriaBuilder criteriaBuilder,
                                             Root<Item> itemRoot,
                                             FilterInfo filterInfo
    ) {
        return criteriaBuilder.isTrue(
                itemRoot.get(Item_.car)
                        .get(Car_.bodyStyle)
                        .in(filterInfo.getBodyStyles())
        );
    }

    private static Predicate carMileagePredicate(CriteriaBuilder criteriaBuilder,
                                          Root<Item> itemRoot,
                                          Integer m
    ) {
        return criteriaBuilder.le(itemRoot.get(Item_.car).get(Car_.mileage), m);
    }

    private static Predicate carPricePredicate(CriteriaBuilder criteriaBuilder,
                                        Root<Item> itemRoot,
                                        Integer p
    ) {
        return criteriaBuilder.le(itemRoot.get(Item_.car).get(Car_.price), p);
    }

    private static Predicate itemTypesPredicate(CriteriaBuilder criteriaBuilder,
                                         Root<Item> itemRoot,
                                         FilterInfo filterInfo
    ) {
        return criteriaBuilder.isTrue(
                itemRoot.get(Item_.type)
                        .in(filterInfo.getItemTypes())
        );
    }

    private static Predicate itemHasPhotosPredicate(CriteriaBuilder criteriaBuilder,
                                             Root<Item> itemRoot,
                                             boolean check
    ) {
        Expression<Set<Long>> itemPhotos = itemRoot.get(Item_.photoIds);
        return check ? criteriaBuilder.isNotEmpty(itemPhotos) : criteriaBuilder.isEmpty(itemPhotos).not();
    }

    private static Predicate activeItemPredicate(CriteriaBuilder criteriaBuilder,
                                          Root<Item> itemRoot,
                                          boolean check
    ) {
        Path<Boolean> itemActivated = itemRoot.get(Item_.isActive);
        return check ? criteriaBuilder.isTrue(itemActivated) : criteriaBuilder.isFalse(itemActivated);
    }

    private static Predicate itemCreationDatePredicate(CriteriaBuilder criteriaBuilder,
                                                Root<Item> itemRoot,
                                                LocalDateTime createdAfter
    ) {
        return criteriaBuilder.greaterThanOrEqualTo(itemRoot.get(Item_.createdAt), createdAfter);
    }

}
