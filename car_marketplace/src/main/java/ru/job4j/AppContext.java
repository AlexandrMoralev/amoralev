package ru.job4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.entity.*;
import ru.job4j.entity.enumerations.*;
import ru.job4j.persistence.impl.*;
import ru.job4j.service.*;
import ru.job4j.service.impl.*;
import ru.job4j.ui.cache.Cache;
import ru.job4j.ui.cache.InMemoryCache;
import ru.job4j.util.HibernateUtils;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

/**
 * AppContext - bean storage
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public enum AppContext {

    INSTANCE;

    private final Logger log = LogManager.getLogger(AppContext.class);

    public final Config config;

    public final HibernateUtils daoUtils;

    public final CarDao carDao;
    public final EngineDao engineDao;
    public final ItemsDao itemsDao;
    public final PhotoDao photoDao;
    public final ProductionInfoDao productionInfoDao;
    public final TransmissionDao transmissionDao;
    public final UsersDao usersDao;

    public final FilterInfoService filterInfoService;
    public final ItemsService itemsService;
    public final PhotoService photoService;
    public final SecurityService securityService;
    public final AuthService authService;
    public final ValidationService validationService;

    public final Cache cache;

    private static byte[] readFile(String filename) {
        try (InputStream is = AppContext.class.getClassLoader().getResourceAsStream(filename)) {
            return is.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    AppContext() {
        config = new Config("car_marketplace.properties");
        String hibernateConfig = config.getString("hibernate.config.filename");
        daoUtils = new HibernateUtils(hibernateConfig);

        carDao = new CarDao(daoUtils);
        engineDao = new EngineDao(daoUtils);
        itemsDao = new ItemsDao(daoUtils);
        photoDao = new PhotoDao(daoUtils);
        productionInfoDao = new ProductionInfoDao(daoUtils);
        transmissionDao = new TransmissionDao(daoUtils);
        usersDao = new UsersDao(daoUtils);

        filterInfoService = new FilterInfoServiceImpl();

        photoService = new PhotoServiceImpl(photoDao);
        securityService = new SecurityServiceImpl(usersDao);
        itemsService = new ItemsServiceImpl(
                itemsDao,
                usersDao,
                carDao,
                productionInfoDao,
                transmissionDao,
                engineDao
        );
        authService = new AuthServiceImpl(
                securityService,
                usersDao
        );

        validationService = new ValidationServiceImpl(
                filterInfoService
        );

        cache = new InMemoryCache(config);

        init(carDao, engineDao, itemsDao, photoDao, productionInfoDao, transmissionDao, usersDao);
    }


    // TODO test data - remove after testing
    private void init(
            CarDao carDao,
            EngineDao engineDao,
            ItemsDao itemsDao,
            PhotoDao photoDao,
            ProductionInfoDao productionInfoDao,
            TransmissionDao transmissionDao,
            UsersDao usersDao
    ) {

        LocalDateTime now = LocalDateTime.now();

        User user1 = User.create("+7(999)000-11-22", "Exclusive dealer", "salt", "password", now.minusDays(2));
        usersDao.save(user1);

        Engine audiEngine = Engine.create("audi engine", 180, 2000, EngineType.DIESEL);
        engineDao.save(audiEngine);

        Transmission audiTransmission = Transmission.create("audi transmission", TransmissionType.AUTOMANUAL);
        transmissionDao.save(audiTransmission);

        ProductionInfo audiProductionInfo = ProductionInfo.create(
                Make.AUDI,
                now.minusMonths(128),
                audiEngine,
                audiTransmission,
                DriveType.ALL_WHEEL_DRIVE,
                Color.GREEN
        );
        productionInfoDao.save(audiProductionInfo);

        Car audi = Car.create(
                audiProductionInfo,
                BodyStyle.HATCHBACK,
                50000,
                1500000L
        );
        carDao.save(audi);

        Item audiItem = Item.create(
                ItemType.CAR,
                user1,
                audi,
                true
        );

        Photo photo1 = Photo.create("photo1", readFile("test_photos/1.jpg"));
        Photo photo2 = Photo.create("photo2", readFile("test_photos/2.jpg"));
        List.of(photo1, photo2).forEach(photoDao::save);

        audiItem.addPhotoIds(List.of(photo1.getId(), photo2.getId()));
        itemsDao.save(audiItem);

        User user2 = User.create("+7(111)222-33-55", "Ivan Ivanych", "salt", "password", now.minusDays(1));
        usersDao.save(user2);

        Engine teslaEngine = Engine.create("tesla engine", 1800, 0, EngineType.ELECTRIC);
        engineDao.save(teslaEngine);

        Transmission teslaTransmission = Transmission.create("tesla transmission", TransmissionType.AUTOMATIC);
        transmissionDao.save(teslaTransmission);

        ProductionInfo teslaProductionInfo = ProductionInfo.create(
                Make.TESLA,
                now.minusMonths(120),
                teslaEngine,
                teslaTransmission,
                DriveType.ALL_WHEEL_DRIVE,
                Color.WHITE
        );
        productionInfoDao.save(teslaProductionInfo);

        Car tesla = Car.create(
                teslaProductionInfo,
                BodyStyle.SEDAN,
                10000,
                3500000L
        );
        carDao.save(tesla);

        Item teslaItem = Item.create(
                ItemType.CAR,
                user2,
                tesla,
                true
        );
        Photo photo3 = Photo.create("photo3", readFile("test_photos/3.jpg"));
        Photo photo4 = Photo.create("photo4", readFile("test_photos/4.jpg"));
        photoDao.savePhotos(List.of(photo3, photo4));

        teslaItem.addPhotoIds(List.of(photo3.getId(), photo4.getId()));
        itemsDao.save(teslaItem);

    }

}