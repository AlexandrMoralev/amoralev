package ru.job4j.persistence.impl;

import ru.job4j.entity.ProductionInfo;
import ru.job4j.persistence.AbstractDao;
import ru.job4j.util.HibernateUtils;

public class ProductionInfoDao extends AbstractDao<ProductionInfo> {

    public ProductionInfoDao(HibernateUtils db) {
        super(db, ProductionInfo.class);
    }
}
