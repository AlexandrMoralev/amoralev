package ru.job4j.mapping.modelsrelations;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import ru.job4j.HibernateUtils;

/**
 * AppContext
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public enum TestAppContext {

    INSTANCE;

    TestAppContext() {
    }

    public static HibernateUtils createAnnotationBasedUtils() {
        return new HibernateUtils(
                new MetadataSources(
                        new StandardServiceRegistryBuilder()
                                .configure("test_annotations_hibernate.cfg.xml")
                                .build()
                )
                        .buildMetadata()
                        .buildSessionFactory()
        );
    }

    public static HibernateUtils createXmlBasedUtils() {

        ServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                .configure("test_xml_hibernate.cfg.xml")
                .build();

        Metadata metadata = new MetadataSources(standardRegistry)
                .addResource("hbm/Car.hbm.xml")
                .addResource("hbm/Driver.hbm.xml")
                .addResource("hbm/Engine.hbm.xml")
                .getMetadataBuilder()
                .applyImplicitNamingStrategy(ImplicitNamingStrategyJpaCompliantImpl.INSTANCE)
                .build();

        return new HibernateUtils(metadata.buildSessionFactory());
    }
}
