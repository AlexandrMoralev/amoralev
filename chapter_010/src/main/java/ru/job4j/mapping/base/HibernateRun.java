package ru.job4j.mapping.base;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class HibernateRun {

    public static void main(String[] args) {

        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();

        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Role role = create(Role.of("ADMIN"), sf);
            create(User.of("Petr Arsentev", role), sf);
            for (User user: findAll(User.class, sf)) {
                System.out.println(String.format("%s %s", user.getName(), user.getRole().getName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public static <T> T create(T model, SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(model);
        session.getTransaction().commit();
        session.close();
        return model;
    }

    public static <T> List<T> findAll(Class<T> clazz, SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<T> list = session.createQuery("from " + clazz.getName(), clazz).list();
        session.getTransaction().commit();
        session.close();
        return list;
    }
}
