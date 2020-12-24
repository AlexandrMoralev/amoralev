package ru.job4j.persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.TestAppContext;
import ru.job4j.entity.Photo;
import ru.job4j.persistence.impl.PhotoDao;

import java.io.InputStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PhotoDaoTest {

    private static final byte[] PHOTO_1;
    private static final byte[] PHOTO_2;
    private static final byte[] PHOTO_3;

    private final TestAppContext context = TestAppContext.INSTANCE;
    private final PhotoDao photoDao = context.photoDao;

    static {
        PHOTO_1 = readFile("test_photos/1.jpg");
        PHOTO_2 = readFile("test_photos/2.jpg");
        PHOTO_3 = readFile("test_photos/3.jpg");
    }

    private static byte[] readFile(String filename) {
        try (InputStream is = PhotoDaoTest.class.getClassLoader().getResourceAsStream(filename)) {
            return is.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void cleanUp() {
        this.photoDao.findAll().forEach(photoDao::delete);
    }

    @Test
    public void whenAddPhotoThenStorageShouldContainTheSameEntity() {
        Photo photo1 = Photo.create("car 1", PHOTO_1.clone());
        Photo photo2 = Photo.create("car 2", PHOTO_2.clone());

        this.photoDao.save(photo1);
        this.photoDao.save(photo2);

        Photo result1 = this.photoDao.find(photo1.getId()).orElseThrow(() -> new RuntimeException("photo_1 not found"));
        Photo result2 = this.photoDao.find(photo2.getId()).orElseThrow(() -> new RuntimeException("photo_2 not found"));

        assertEquals(2, this.photoDao.findAll().size());

        assertEquals("car 1", result1.getName());
        assertEquals("car 2", result2.getName());
        assertArrayEquals(PHOTO_1, result1.getImage());
        assertArrayEquals(PHOTO_2, result2.getImage());
    }

    @Test
    public void createDuplicatePhotoDoesntCauseErrors() {
        Photo photo = Photo.create("car 1", PHOTO_1.clone());
        Photo duplicate = Photo.create("car 1", PHOTO_1.clone());

        this.photoDao.save(photo);
        this.photoDao.save(duplicate);
        Optional<Photo> t = this.photoDao.find(photo.getId());
        Optional<Photo> d = this.photoDao.find(duplicate.getId());

        assertEquals("car 1", t.get().getName());
        assertArrayEquals(PHOTO_1, t.get().getImage());
        assertEquals(2, this.photoDao.findAll().size());
        assertTrue(d.isPresent());
    }

    @Test
    public void whenUpdatePhotoThenStorageShouldContainTheUpdatedEntity() {
        Photo photo1 = Photo.create("car 1", PHOTO_1.clone());
        Photo photo2 = Photo.create("car 2", PHOTO_2.clone());

        this.photoDao.save(photo1);
        this.photoDao.save(photo2);
        Optional<Photo> saved1 = this.photoDao.find(photo1.getId());
        Optional<Photo> saved2 = this.photoDao.find(photo2.getId());

        assertTrue(saved1.isPresent());
        assertTrue(saved2.isPresent());
        assertEquals(2, this.photoDao.findAll().size());

        saved1.map(photo -> {
            photo.setName("updated car 1");
            photo.setImage(PHOTO_3.clone());
            return photo;
        }).ifPresent(this.photoDao::update);

        assertEquals(2, this.photoDao.findAll().size());

        Optional<Photo> updated = this.photoDao.find(photo1.getId());
        Optional<Photo> notUpdated = this.photoDao.find(photo2.getId());

        assertEquals("updated car 1", updated.get().getName());
        assertEquals("car 2", notUpdated.get().getName());

        assertArrayEquals(PHOTO_3, updated.get().getImage());
        assertArrayEquals(PHOTO_2, notUpdated.get().getImage());
    }

    @Test
    public void whenDeletePhotoThenStorageShouldNotContainTheEntity() {
        Photo photo1 = Photo.create("car 1", PHOTO_1.clone());
        Photo photo2 = Photo.create("car 2", PHOTO_2.clone());
        Photo photo3 = Photo.create("car 3", PHOTO_3.clone());

        this.photoDao.save(photo1);
        this.photoDao.save(photo2);
        this.photoDao.save(photo3);
        Optional<Photo> result1 = this.photoDao.find(photo1.getId());
        Optional<Photo> result2 = this.photoDao.find(photo2.getId());
        Optional<Photo> result3 = this.photoDao.find(photo3.getId());

        assertTrue(result1.isPresent());
        assertTrue(result2.isPresent());
        assertTrue(result3.isPresent());

        assertEquals(3, this.photoDao.findAll().size());

        this.photoDao.delete(photo1);
        this.photoDao.deleteById(photo2.getId());

        assertTrue(this.photoDao.find(photo1.getId()).isEmpty());
        assertTrue(this.photoDao.find(photo2.getId()).isEmpty());
        assertEquals(result3.get(), this.photoDao.findAll().iterator().next());
    }
}
