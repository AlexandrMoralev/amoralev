package ru.job4j.persistence.impl;

import org.hibernate.query.Query;
import ru.job4j.entity.Photo;
import ru.job4j.persistence.AbstractDao;
import ru.job4j.util.HibernateUtils;

import java.util.Collection;

public class PhotoDao extends AbstractDao<Photo> {

    public PhotoDao(HibernateUtils db) {
        super(db, Photo.class);
    }

    public Collection<Photo> savePhotos(Collection<Photo> photos) {
        return db.tx(
                session -> {
                    photos.forEach(session::saveOrUpdate);
                    return photos;
                }
        );
    }

    public Collection<Photo> getPhotos(Collection<Long> photoIds) {
        return db.tx(
                session -> {
                    Query query = session.createQuery("from " + clazz.getName() + " where photo_id in (:ids)");
                    query.setParameterList("ids", photoIds);
                    return (Collection<Photo>) query.list();
                }
        );
    }

    public void deletePhotos(Collection<Long> photoIds) {
        db.tx(
                session -> {
                    Query query = session.createQuery("delete from " + clazz.getName() + " where photo_id in (:ids)");
                    query.setParameterList("ids", photoIds);
                    query.executeUpdate();
                }
        );
    }

}
