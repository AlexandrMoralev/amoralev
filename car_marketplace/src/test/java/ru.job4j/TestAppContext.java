package ru.job4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.persistence.impl.*;
import ru.job4j.service.*;
import ru.job4j.service.impl.*;
import ru.job4j.util.HibernateUtils;

/**
 * TestAppContext - test beans storage
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public enum TestAppContext {

    INSTANCE;

    private final Logger log = LogManager.getLogger(TestAppContext.class);

    public final Config config;

    public final HibernateUtils daoUtils;
    public final TestUtils testUtils;

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

    TestAppContext() {
        config = new Config("test_car_marketplace.properties");
        daoUtils = new HibernateUtils("test.hibernate.cfg.xml");
        testUtils = TestUtils.INSTANCE;

        carDao = new CarDao(daoUtils);
        engineDao = new EngineDao(daoUtils);
        itemsDao = new ItemsDao(daoUtils);
        photoDao = new PhotoDao(daoUtils);
        productionInfoDao = new ProductionInfoDao(daoUtils);
        transmissionDao = new TransmissionDao(daoUtils);
        usersDao = new UsersDao(daoUtils);

        filterInfoService = new FilterInfoServiceImpl();
        photoService = new PhotoServiceImpl(photoDao);
        itemsService = new ItemsServiceImpl(
                itemsDao,
                usersDao,
                carDao,
                productionInfoDao,
                transmissionDao,
                engineDao
        );
        securityService = new SecurityServiceImpl(usersDao);
        authService = new AuthServiceImpl(
                securityService,
                usersDao
        );

        validationService = new ValidationServiceImpl(
                filterInfoService
        );

        log.info("Test app context initialized.");
    }

}