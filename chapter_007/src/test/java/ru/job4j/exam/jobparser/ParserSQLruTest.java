package ru.job4j.exam.jobparser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.LocalDateTime;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * ParserSQLruTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class ParserSQLruTest {

    private final static Logger LOG = LogManager.getLogger(ParserSQLruTest.class);
    private static Config config;
    private PageCounter pageCounter;


    @BeforeClass
    public static void init() {
        config = new Config("jobparser_app.properties");
        LOG.info(config.toString());
    }

    @BeforeEach
    public void beforeEach() {
        pageCounter = new SqlRuPageCounter();
    }

    @Test
    public void parsePageInMemmoryStoreManualTest() throws Exception {
        Store<Vacancy> store = new InMemoryStore(config);
        store.deleteAll();
        ParserSQLru parserSQLru = new ParserSQLru(config, store, pageCounter);
        store.addAll(parserSQLru.parsePage("https://www.sql.ru/forum/job-offers/"));
        List<Vacancy> vacancies = new ArrayList<>(store.findAll());
        printList(vacancies);
    }

    @Test
    public void searchVacanciesInMemmoryStoreManualTest() throws Exception {
        Store<Vacancy> store = new InMemoryStore(config);
        store.deleteAll();
        ParserSQLru parserSQLru = new ParserSQLru(config, store, pageCounter);
        parserSQLru.searchVacanciesToDate(LocalDateTime.now().minusDays(365));
        List<Vacancy> vacancies = new ArrayList<>(store.findAll());
        printList(vacancies);
    }

    @Test
    public void parsePageDbStoreManualTest() throws Exception {
        Store<Vacancy> store = new StoreDB(config);
        store.deleteAll();
        ParserSQLru parserSQLru = new ParserSQLru(config, store, pageCounter);
        store.addAll(parserSQLru.parsePage("https://www.sql.ru/forum/job-offers/"));
        List<Vacancy> vacancies = new ArrayList<>(store.findAll());
        printList(vacancies);
    }

    @Test
    public void searchVacanciesDbStoreManualTest() throws Exception {
        Store<Vacancy> store = new StoreDB(config);
        store.deleteAll();
        ParserSQLru parserSQLru = new ParserSQLru(config, store, pageCounter);
        parserSQLru.searchVacanciesToDate(LocalDateTime.now().minusDays(365));
        List<Vacancy> vacancies = new ArrayList<>(store.findAll());
        printList(vacancies);
    }

    private void printList(List<?> objects) {
        for (int i = 0; i < objects.size(); i++) {
            LOG.debug(String.format("#%s - %s", i + 1, objects.get(i).toString()));
        }
    }
}
