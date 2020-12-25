package ru.job4j.service;

import ru.job4j.entity.Photo;
import ru.job4j.exception.ConsumerEx;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * PhotoService
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface PhotoService {

    Photo savePhoto(Photo photo);

    Optional<Photo> getPhoto(Long photoId);

    void deletePhoto(Long photoId);

    Collection<Photo> savePhotos(Collection<Photo> photos);

    Collection<Photo> getPhotos(Collection<Long> photoIds);

    void deletePhotos(Collection<Long> photoIds);

    default <Photo> Consumer<Photo> throwingConsumerWrapper(
            ConsumerEx<Photo> consumerEx) {
        return p -> {
            try {
                consumerEx.accept(p);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

}
