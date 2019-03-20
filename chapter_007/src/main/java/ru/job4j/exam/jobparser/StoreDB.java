package ru.job4j.exam.jobparser;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
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
    private final DbProperties db;
    private Connection connection;
    //TODO add logger

    public StoreDB(final Config config) {
        this.config = config;
        this.config.init();
        this.db = new DbProperties();
        this.connection = getConnection();
        checkDBStructure();
    }

    private Connection getConnection() {
        final File dbFile = new File(".");
        loadDbDriver();
        Connection connect = null;
        for (int i = 0; i < this.db.attemptsToConnect; i++) {
            try {
                connect = DriverManager.getConnection(
                        String.format("%s%s%s%s",
                                this.db.url,
                                dbFile.getAbsolutePath(),
                                File.pathSeparator,
                                this.db.dbName),
                        this.db.user,
                        this.db.pass
                );
                if (connect != null) {
                    break;
                }
                Thread.sleep(50);
            } catch (SQLException | InterruptedException e) {
                e.printStackTrace(); //TODO logger
            }
        }
        return connect;
    }

    private void loadDbDriver() {
        try {
            Class.forName(this.db.driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); //TODO logger
        }
    }

    private void checkConnection() {
        try {
            if (this.connection == null || this.connection.isClosed()) {
                this.connection = getConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace(); //TODO logger
        }
    }

    private void checkDBStructure() {
        final String CREATE_TABLE_IF_NOT_EXISTS = String
                .format("CREATE TABLE IF NOT EXISTS %s ( %s SERIAL PRIMARY KEY, %s VARCHAR(200) NOT NULL, %s TEXT NOT NULL, %s VARCHAR(512) NOT NULL, %s DATE NOT NULL CONSTRAINT %s UNIQUE);",
                        this.db.table,
                        this.db.itemId,
                        this.db.itemName,
                        this.db.itemDesc,
                        this.db.itemLink,
                        this.db.itemDate,
                        this.db.itemName
                );
        try (PreparedStatement ps = this.connection.prepareStatement(CREATE_TABLE_IF_NOT_EXISTS)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); //TODO logger
        }
    }

    private void validate(Object object) {
        if (object == null) {
            throw new NullPointerException();
        }
    }

    public int add(final Vacancy vacancy) {
        validate(vacancy);
        final String INSERT_INTO_TABLE = String.format("INSERT INTO %s VALUES (%s, %s, %s, %s);",
                this.db.table,
                vacancy.getName(),
                vacancy.getDescription(),
                vacancy.getLink(),
                vacancy.getCreatedAsTimeStamp()
        );
        int added = 0;
        checkConnection();
        try (PreparedStatement ps = this.connection.prepareStatement(INSERT_INTO_TABLE)) {
            added = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); //TODO logger
        }
        return added;
    }

    public int[] addAll(final List<Vacancy> vacancies) {
        validate(vacancies);
        int[] empty = new int[0];
        if (vacancies.isEmpty()) {
            return empty;
        }
        checkConnection();
        int[] result = null;
        try (Statement statement = connection.createStatement()) {
            this.connection.setAutoCommit(false);
            for (Vacancy vacancy : vacancies) {
                statement.addBatch(String.format("INSERT INTO %s VALUES (%s, %s, %s, %s);",
                        this.db.table,
                        vacancy.getName(),
                        vacancy.getDescription(),
                        vacancy.getLink(),
                        vacancy.getCreatedAsTimeStamp())
                );
            }
            result = statement.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace(); //TODO logger
        }
        return result != null ? result : empty;
    }

    public Optional<Vacancy> findByName(final String name) {
        validate(name);
        Optional<Vacancy> result = Optional.empty();
        if (name.isBlank()) {
            return result;
        }
        checkConnection();
        final String SELECT = String.format("SELECT * FROM %s WHERE %s = %s;",
                this.db.table,
                this.db.itemName,
                name
        );
        try (PreparedStatement ps = this.connection.prepareStatement(SELECT)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = Optional.of(createItem(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();  //TODO logger
        }
        return result;
    }

    public List<Vacancy> findByDate(final String date) {

        return Collections.emptyList();
    }

    public List<Vacancy> findByPeriod(final String fromDate, final String toDate) {

        return Collections.emptyList();
    }

    public List<Vacancy> findRecent(final int number) {
        if (number < 0) {
            throw new IllegalStateException();
        }
        if (number == 0) {
            return Collections.emptyList();
        }
        checkConnection();
        final String SELECT = String.format("SELECT * FROM %s GROUP BY %s LIMIT %s;",
                this.db.table,
                this.db.itemDate,
                number
        );
        List<Vacancy> result = new ArrayList<>();
        try (PreparedStatement ps = this.connection.prepareStatement(SELECT)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(createItem(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace(); //TODO logger
        }
        return result.isEmpty() ? Collections.emptyList() : result;
    }

    public int deleteByName(final String name) {
        validate(name);
        int updated = 0;
        if (name.isBlank()) {
            return updated;
        }
        final String DELETE = String.format("DELETE FROM %s WHERE %s = %s;",
                this.db.table,
                this.db.itemName,
                name
        );
        try (PreparedStatement ps = this.connection.prepareStatement(DELETE)) {
            updated = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); //TODO logger
        }
        return updated;
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

    private Vacancy createItem(final ResultSet resultSet) throws SQLException {
        return new Vacancy(
                resultSet.getString(this.db.itemName),
                resultSet.getString(this.db.itemDesc),
                resultSet.getString(this.db.itemLink),
                resultSet.getTimestamp(this.db.itemDate) //TODO define date-converter class
        );
    }

    @Override
    public void close() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace(); //TODO logger
        }
    }

    private final class DbProperties {
        private final String driver = config.get("jdbc.driver");
        private final int attemptsToConnect = Integer.parseInt(config.get("jdbc.attempts"));
        private final String dbName = config.get("db.name");
        private final String url = config.get("jdbc.url");
        private final String user = config.get("jdbc.username");
        private final String pass = config.get("jdbc.password");
        private final String table = config.get("db.table.name");
        private final String itemId = config.get("db.table.item_id");
        private final String itemName = config.get("db.table.item_name");
        private final String itemDesc = config.get("db.table.item_desc");
        private final String itemLink = config.get("db.table.item_link");
        private final String itemDate = config.get("db.table.item_date");
    }
}
