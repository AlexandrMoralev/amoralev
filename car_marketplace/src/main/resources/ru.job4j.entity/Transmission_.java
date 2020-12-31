package ru.job4j.entity;

import ru.job4j.entity.enumerations.TransmissionType;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Transmission.class)
public abstract class Transmission_ {

	public static volatile SingularAttribute<Transmission, String> model;
	public static volatile SingularAttribute<Transmission, Long> id;
	public static volatile SingularAttribute<Transmission, TransmissionType> type;

	public static final String MODEL = "model";
	public static final String ID = "id";
	public static final String TYPE = "type";

}

