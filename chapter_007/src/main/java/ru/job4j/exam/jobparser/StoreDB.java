package ru.job4j.exam.jobparser;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * StoreDB - class for interacting with the current database;
 * supports CRUD operations on the current DB
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class StoreDB implements AutoCloseable {
    private final Config config;
    private Connection connection;
    //TODO add logger

    public StoreDB(final Config config) {
        this.config = config;
        checkConnection();
        checkDBStructure();
    }

    private void checkConnection() {

    }

    private void checkDBStructure() {

    }

    public int add(final List<Vacancy> vacancies) {

        return 0;
    }

    public int update(final List<Vacancy> updates) {

        return 0;
    }

    public Optional<Vacancy> findByName(final String name) {

        return Optional.empty();
    }

    public List<Vacancy> findByDate(final String date) {

        return Collections.emptyList();
    }

    public List<Vacancy> findByPeriod(final String fromDate, final String toDate) {

        return Collections.emptyList();
    }

    public List<Vacancy> findRecent(final int number) {

        return Collections.emptyList();
    }

    public boolean deleteByName(final String name) {

        return false;
    }

    public int deleteOlderThan(final String date) {

        return 0;
    }

    public int deleteByDate(final String date) {

        return 0;
    }

    public int deleteByPeriod(final String fromDate, final String toDate) {

        return 0;
    }


    @Override
    public void close() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
