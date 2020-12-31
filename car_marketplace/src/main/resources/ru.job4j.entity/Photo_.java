package ru.job4j.entity;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.time.LocalDateTime;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Photo.class)
public abstract class Photo_ {

	public static volatile SingularAttribute<Photo, byte[]> image;
	public static volatile SingularAttribute<Photo, LocalDateTime> createdAt;
	public static volatile SingularAttribute<Photo, Item> item;
	public static volatile SingularAttribute<Photo, String> name;
	public static volatile SingularAttribute<Photo, Long> id;

	public static final String IMAGE = "image";
	public static final String CREATED_AT = "createdAt";
	public static final String ITEM = "item";
	public static final String NAME = "name";
	public static final String ID = "id";

}

