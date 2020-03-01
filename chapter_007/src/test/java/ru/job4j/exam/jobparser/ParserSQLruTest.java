package ru.job4j.exam.jobparser;

import org.junit.Test;

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

    @Test
    public void parsePageManualTest() throws Exception {
        Config config = new Config("jobparser_app.properties");
        System.out.println(config.toString());
        Store<Vacancy> store = new InMemoryStore(config);
        PageCounter pageCounter = new SqlRuPageCounter();

        ParserSQLru parserSQLru = new ParserSQLru(config, store, pageCounter);

        List<Vacancy> vacancies = new ArrayList<>(parserSQLru.parsePage("https://www.sql.ru/forum/job-offers/"));
        System.out.println();
        System.out.println("==================================================================================");
        System.out.println();

        for (int i = 0; i < vacancies.size(); i++) {
            System.out.println(String.format("%s - %s", i+1, vacancies.get(i).toString()));
        }
    }
}
