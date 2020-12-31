package ru.job4j.persistence.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.job4j.entity.Item;
import ru.job4j.entity.Item_;
import ru.job4j.persistence.AbstractDao;
import ru.job4j.persistence.FilterInfoResolver;
import ru.job4j.ui.dto.FilterInfo;
import ru.job4j.util.HibernateUtils;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class ItemsDao extends AbstractDao<Item> {

    private final String selectAll;
    private final String orderByCreatedAt;
    private final String findActive;

    public ItemsDao(HibernateUtils db) {
        super(db, Item.class);

        this.selectAll = "from " + clazz.getName();
        this.orderByCreatedAt = " order by date(created_at) desc ";
        this.findActive = selectAll + " where is_active = true" + orderByCreatedAt;

    }

    @Override
    public Stream<Item> findAll(FilterInfo filterInfo) {  // TODO use entity graph
        return db.tx(
                session -> {
                    return findItemsByFilterInfo(session, filterInfo, FilterInfoResolver::createItemsPredicates);
                }
        );
    }

    public Stream<Item> findByUser(Integer userId) {
        return db.tx(
                session -> {
                    Query query = session.createQuery(selectAll + " where user_id = :userId" + orderByCreatedAt);
                    query.setParameter("userId", userId);
                    return (Stream<Item>) query.list().stream();
                }
        );
    }

    public Stream<Item> findActive() {
        return db.tx(
                session -> {
                    Query query = session.createQuery(findActive);
                    return (Stream<Item>) query.list().stream();
                }
        );
    }

    public Stream<Item> findActive(FilterInfo filterInfo) {
        return db.tx(
                session -> {
                    return findItemsByFilterInfo(session, filterInfo, FilterInfoResolver::createActiveItemsPredicates);
                }
        );
    }

    private Stream<Item> findItemsByFilterInfo(Session session,
                                               FilterInfo filterInfo,
                                               Function<FilterInfoResolver, Stream<javax.persistence.criteria.Predicate>> filterInfoResolverFunction
    ) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery query = criteriaBuilder.createQuery(Item.class);
        Root<Item> itemRoot = query.from(Item.class);

        Optional<FilterInfoResolver> filterInfoResolver = FilterInfoResolver.of(criteriaBuilder, itemRoot, filterInfo);
        Order createdAtDesc = criteriaBuilder.desc(itemRoot.get(Item_.createdAt));

        CriteriaQuery queryBuilder = query.select(itemRoot)
                .distinct(true);
        filterInfoResolver
                .map(filterInfoResolverFunction)
                .map(v -> v.toArray(javax.persistence.criteria.Predicate[]::new))
                .ifPresent(queryBuilder::where);
        queryBuilder.orderBy(createdAtDesc);

        TypedQuery<Item> typedQuery = session.createQuery(query);
        return typedQuery.getResultList().stream();
    }

}