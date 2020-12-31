package ru.job4j.entity;

import ru.job4j.entity.enumerations.BodyStyle;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Car.class)
public abstract class Car_ {

	public static volatile SingularAttribute<Car, ProductionInfo> productionInfo;
	public static volatile SingularAttribute<Car, Long> price;
	public static volatile SingularAttribute<Car, BodyStyle> bodyStyle;
	public static volatile SingularAttribute<Car, Long> id;
	public static volatile SingularAttribute<Car, Integer> mileage;

	public static final String PRODUCTION_INFO = "productionInfo";
	public static final String PRICE = "price";
	public static final String BODY_STYLE = "bodyStyle";
	public static final String ID = "id";
	public static final String MILEAGE = "mileage";

}

