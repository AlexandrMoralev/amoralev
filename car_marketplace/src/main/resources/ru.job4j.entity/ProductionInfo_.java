package ru.job4j.entity;

import ru.job4j.entity.enumerations.Color;
import ru.job4j.entity.enumerations.DriveType;
import ru.job4j.entity.enumerations.Make;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.time.LocalDateTime;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductionInfo.class)
public abstract class ProductionInfo_ {

	public static volatile SingularAttribute<ProductionInfo, LocalDateTime> producedAt;
	public static volatile SingularAttribute<ProductionInfo, Transmission> transmission;
	public static volatile SingularAttribute<ProductionInfo, Color> color;
	public static volatile SingularAttribute<ProductionInfo, Engine> engine;
	public static volatile SingularAttribute<ProductionInfo, DriveType> driveType;
	public static volatile SingularAttribute<ProductionInfo, Long> id;
	public static volatile SingularAttribute<ProductionInfo, Make> make;

	public static final String PRODUCED_AT = "producedAt";
	public static final String TRANSMISSION = "transmission";
	public static final String COLOR = "color";
	public static final String ENGINE = "engine";
	public static final String DRIVE_TYPE = "driveType";
	public static final String ID = "id";
	public static final String MAKE = "make";

}

