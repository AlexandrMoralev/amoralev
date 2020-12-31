package ru.job4j.entity;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.time.LocalDateTime;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public abstract class User_ {

	public static volatile SingularAttribute<User, String> password;
	public static volatile SingularAttribute<User, String> salt;
	public static volatile SingularAttribute<User, String> phone;
	public static volatile SingularAttribute<User, String> name;
	public static volatile SingularAttribute<User, LocalDateTime> registeredAt;
	public static volatile SingularAttribute<User, Integer> id;
	public static volatile SetAttribute<User, Item> items;

	public static final String PASSWORD = "password";
	public static final String SALT = "salt";
	public static final String PHONE = "phone";
	public static final String NAME = "name";
	public static final String REGISTERED_AT = "registeredAt";
	public static final String ID = "id";
	public static final String ITEMS = "items";

}

