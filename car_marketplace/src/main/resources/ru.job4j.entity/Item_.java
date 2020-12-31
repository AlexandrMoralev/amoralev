package ru.job4j.entity;

import ru.job4j.entity.enumerations.ItemType;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.time.LocalDateTime;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Item.class)
public abstract class Item_ {

	public static volatile SingularAttribute<Item, LocalDateTime> createdAt;
	public static volatile SingularAttribute<Item, User> createdBy;
	public static volatile SingularAttribute<Item, Car> car;
	public static volatile SingularAttribute<Item, Integer> id;
	public static volatile SingularAttribute<Item, ItemType> type;
	public static volatile SingularAttribute<Item, Boolean> isActive;
	public static volatile SetAttribute<Item, Long> photoIds;

	public static final String CREATED_AT = "createdAt";
	public static final String CREATED_BY = "createdBy";
	public static final String CAR = "car";
	public static final String ID = "id";
	public static final String TYPE = "type";
	public static final String IS_ACTIVE = "isActive";
	public static final String PHOTO_IDS = "photoIds";

}

