package ru.job4j.service.impl;

import ru.job4j.entity.Photo;
import ru.job4j.persistence.impl.PhotoDao;
import ru.job4j.service.PhotoService;

import java.util.Collection;
import java.util.Optional;

/**
 * PhotoServiceImpl
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class PhotoServiceImpl implements PhotoService {

    private final PhotoDao photoDao;

    public PhotoServiceImpl(PhotoDao photoDao) {
        this.photoDao = photoDao;
    }

    @Override
    public Photo savePhoto(Photo photo) {
        this.photoDao.save(photo);
        return photo;
    }

    @Override
    public Optional<Photo> getPhoto(Long photoId) {
        return this.photoDao.find(photoId);
    }

    @Override
    public void deletePhoto(Long photoId) {
        this.photoDao.deleteById(photoId);
    }

    @Override
    public Collection<Photo> savePhotos(Collection<Photo> photos) {
        return this.photoDao.savePhotos(photos);
    }

    @Override
    public Collection<Photo> getPhotos(Collection<Long> photoIds) {
        return this.photoDao.getPhotos(photoIds);
    }

    @Override
    public void deletePhotos(Collection<Long> photoIds) {
        this.photoDao.deletePhotos(photoIds);
    }

}
