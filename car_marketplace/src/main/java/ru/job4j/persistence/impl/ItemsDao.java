package ru.job4j.persistence.impl;

import org.hibernate.query.Query;
import ru.job4j.entity.Item;
import ru.job4j.persistence.AbstractDao;
import ru.job4j.ui.dto.FilterInfo;
import ru.job4j.util.DateTimeUtils;
import ru.job4j.util.HibernateUtils;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ItemsDao extends AbstractDao<Item> {

    private final String selectAll;
    private final String orderByCreatedAt;
    private final String findAll;
    private final String findActive;

    public ItemsDao(HibernateUtils db) {
        super(db, Item.class);

        this.selectAll = "from " + clazz.getName();
        this.orderByCreatedAt = " order by date(created_at) desc ";
        this.findAll = selectAll + orderByCreatedAt;
        this.findActive = selectAll + " where is_active = true" + orderByCreatedAt;

    }


    @Override
    public Collection<Item> findAll(FilterInfo filterInfo) {  // TODO use entity graph
        List<Predicate<Item>> itemFilters = createFilters(filterInfo);
        return db.tx(
                session -> (List<Item>) session.createQuery(findAll).list()
                        .stream()
                        .filter(itemFilters.stream().reduce(x -> true, Predicate::and))
                        .collect(Collectors.toList())
        );
    }

    public Collection<Item> findByUser(Integer userId) {
        return db.tx(
                session -> {
                    Query query = session.createQuery(selectAll + " where user_id = :userId" + orderByCreatedAt);
                    query.setParameter("userId", userId);
                    return (List<Item>) query.list();
                }
        );
    }

    public Collection<Item> findActive() {
        return db.tx(
                session -> {
                    Query query = session.createQuery(findActive);
                    return (List<Item>) query.list();
                }
        );
    }

    public Collection<Item> findActive(FilterInfo filterInfo) {
        List<Predicate<Item>> itemFilters = createFilters(filterInfo);
        return db.tx(
                session -> {
                    return ((List<Item>) session.createQuery(findActive).list())
                            .stream()
                            .filter(itemFilters.stream().reduce(x -> true, Predicate::and))
                            .collect(Collectors.toList()); // FIXME need to collect?
                }
        );
    }

    private static List<Predicate<Item>> createFilters(FilterInfo filter) { // TODO refactoring, use hibernate features
        Predicate<Item> itemTypesFilter = item -> isRelevant(item.getType(), filter.getItemTypes());
        Predicate<Item> carsMakesFilter = item -> isRelevant(item.getCar().getProductionInfo().getMake(), filter.getMakes());
        Predicate<Item> driveTypeFilter = item -> isRelevant(item.getCar().getProductionInfo().getDriveType(), filter.getDriveTypes());
        Predicate<Item> transmissionTypeFilter = item -> isRelevant(item.getCar().getProductionInfo().getTransmission().getType(), filter.getTransmissionTypes());
        Predicate<Item> engineTypeFilter = item -> isRelevant(item.getCar().getProductionInfo().getEngine().getType(), filter.getEngineTypes());
        Predicate<Item> bodyStyleFilter = item -> isRelevant(item.getCar().getBodyStyle(), filter.getBodyStyles());
        Predicate<Item> carsColorFilter = item -> isRelevant(item.getCar().getProductionInfo().getColor(), filter.getColors());

        Predicate<Item> priceFilter = item -> filter.getPriceMax().map(p -> item.getCar().getPrice() <= p).orElse(true);
        Predicate<Item> mileageFilter = item -> filter.getMileageMax().map(m -> item.getCar().getMileage() <= m).orElse(true);
        Predicate<Item> producedDateFilter = item -> filter.getProducedAfter().map(DateTimeUtils::convertToLocalDateTime).map(p -> item.getCar().getProductionInfo().getProducedAt().isAfter(p)).orElse(true);
        Predicate<Item> hpFilter = item -> filter.getHpMin().map(hp -> item.getCar().getProductionInfo().getEngine().getHp() >= hp).orElse(true);

        return List.of(
                itemTypesFilter,
                priceFilter,
                mileageFilter,
                hpFilter,
                producedDateFilter,
                carsMakesFilter,
                driveTypeFilter,
                transmissionTypeFilter,
                engineTypeFilter,
                bodyStyleFilter,
                carsColorFilter
        );
    }

    private static <T> boolean isRelevant(T element, Collection<T> relevantValues) {
        return relevantValues.isEmpty() || relevantValues.contains(element);
    }

}