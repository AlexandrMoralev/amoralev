package ru.job4j.entity;

import ru.job4j.entity.enumerations.EngineType;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Engine.class)
public abstract class Engine_ {

	public static volatile SingularAttribute<Engine, Integer> volume;
	public static volatile SingularAttribute<Engine, Integer> hp;
	public static volatile SingularAttribute<Engine, String> model;
	public static volatile SingularAttribute<Engine, Long> id;
	public static volatile SingularAttribute<Engine, EngineType> type;

	public static final String VOLUME = "volume";
	public static final String HP = "hp";
	public static final String MODEL = "model";
	public static final String ID = "id";
	public static final String TYPE = "type";

}

