package ru.job4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.domain.Item;
import ru.job4j.domain.User;
import ru.job4j.persistence.Dao;
import ru.job4j.persistence.ItemsDaoImpl;
import ru.job4j.persistence.UsersDaoImpl;
import ru.job4j.security.SecurityService;
import ru.job4j.security.SecurityServiceImpl;
import ru.job4j.service.ValidationService;
import ru.job4j.service.ValidationServiceImpl;
import ru.job4j.ui.cache.Cache;
import ru.job4j.ui.cache.InMemoryCache;
import ru.job4j.util.HibernateUtils;

/**
 * AppContext - bean storage
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public enum TestAppContext {

    INSTANCE;

    private final Logger log = LogManager.getLogger(TestAppContext.class);

    public final HibernateUtils daoUtils;
    public final Dao<Item> itemsDao;
    public final Dao<User> usersDao;
    public final SecurityService securityService;
    public final ValidationService validationService;
    public final Cache cache;

    TestAppContext() {
        Config config = new Config("test_todo_app.properties");
        String hibernateConfig = config.getString("hibernate.config.filename");
        daoUtils = new HibernateUtils(hibernateConfig);
        itemsDao = new ItemsDaoImpl(daoUtils);
        usersDao = new UsersDaoImpl(daoUtils);
        securityService = new SecurityServiceImpl(usersDao);
        validationService = new ValidationServiceImpl(
                securityService,
                usersDao,
                itemsDao);
        cache = new InMemoryCache(config);
        log.info("Test app context initialized.");
    }
}
