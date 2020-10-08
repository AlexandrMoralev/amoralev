package ru.job4j.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import ru.job4j.entity.enumerations.BodyStyle;

import javax.persistence.*;
import java.util.Set;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "cars", schema = "public")
public class Car implements Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id", unique = true, nullable = false)
    private Long id;

    private ProductionInfo productionInfo;

    private BodyStyle bodyStyle;

    private Integer mileage;

    private Long price;

    private User currentOwner;

    private Boolean isActive;

    private Set<Photo> photos;


}
