package ru.job4j.exam.jobparser;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.LocalDateTime;

import java.io.InputStream;
import java.sql.*;
import java.util.*;

import static ru.job4j.exam.jobparser.DateTimeUtil.convertToLocalDateTime;
import static ru.job4j.exam.jobparser.DateTimeUtil.convertToTimestamp;

/**
 * StoreDB - class for interacting with the current database;
 * supports CRUD operations on the current DB
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class StoreDB implements Store<Vacancy>, AutoCloseable {

    private final static Logger LOG = LogManager.getLogger(DateTimeUtil.class);
    private final Config config;
    private final DbProperties db;
    private Connection connection;

    public StoreDB(final Config config) {
        this.config = config;
        this.db = new DbProperties();
        this.init();
    }

    private boolean init() {
        try (InputStream in = StoreDB.class
                .getClassLoader()
                .getResourceAsStream("jobparser_app.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("jdbc.driver"));
            this.connection = DriverManager.getConnection(
                    config.getProperty("jdbc.url"),
                    config.getProperty("jdbc.username"),
                    config.getProperty("jdbc.password")
            );
        } catch (Exception e) {
            LOG.error("DB initialisation error", e);
            throw new RuntimeException("DB error", e);
        }
        checkDBStructure();
        return this.connection != null;
    }

    private void checkDBStructure() {
        final String CREATE_TABLE_IF_NOT_EXISTS = String
                .format("CREATE TABLE IF NOT EXISTS %s ( %s SERIAL PRIMARY KEY, %s VARCHAR(200) NOT NULL, %s TEXT NOT NULL, %s VARCHAR(512) NOT NULL, %s TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (current_timestamp AT TIME ZONE 'UTC') CONSTRAINT %s UNIQUE);",
                        this.db.table,
                        this.db.itemId,
                        this.db.itemName,
                        this.db.itemDesc,
                        this.db.itemLink,
                        this.db.itemCreated,
                        this.db.itemName
                );
        try (PreparedStatement ps = this.connection.prepareStatement(CREATE_TABLE_IF_NOT_EXISTS)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error("DB schema error", e);
            throw new RuntimeException("DB error");
        }
    }

    @Override
    public Optional<String> add(final Vacancy vacancy) {
        final String INSERT_RETURNING = String.format("INSERT INTO %s VALUES (?, ?, ?, ?) RETURNING %s;", this.db.table, this.db.itemId);
        try (PreparedStatement ps = this.connection.prepareStatement(INSERT_RETURNING)) {
            ps.setString(1, vacancy.getName());
            ps.setString(2, vacancy.getDescription());
            ps.setString(3, vacancy.getLink());
            ps.setTimestamp(4, convertToTimestamp(vacancy.getCreated()));
            ps.executeUpdate();
            ResultSet resultSet = ps.getResultSet();
            return resultSet.next() ? Optional.of(String.valueOf(resultSet.getInt(1))) : Optional.empty();
        } catch (SQLException e) {
            LOG.error("DB insert error", e);
            throw new RuntimeException("DB error");
        }
    }

    @Override
    public void addAll(final Collection<Vacancy> vacancies) {
        if (!vacancies.isEmpty()) {
            final String INSERT_VALUES = "INSERT INTO %s VALUES (%s, %s, %s, %s);";
            try (Statement statement = connection.createStatement()) {
                this.connection.setAutoCommit(false);
                for (Vacancy vacancy : vacancies) {
                    statement.addBatch(
                            String.format(INSERT_VALUES,
                                    this.db.table,
                                    vacancy.getName(),
                                    vacancy.getDescription(),
                                    vacancy.getLink(),
                                    convertToTimestamp(vacancy.getCreated()).toString())
                    );
                }
                statement.executeBatch();
                connection.commit();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                LOG.error("DB batch insert error", e);
                throw new RuntimeException("DB error");
            }
        }
    }

    @Override
    public boolean update(Vacancy vacancy) {
        final String UPDATE_TABLE = String.format("UPDATE %s SET (?, ?, ?, ?) WHERE %s = ?;", this.db.table, this.db.itemId);
        try (PreparedStatement ps = this.connection.prepareStatement(UPDATE_TABLE)) {
            ps.setString(1, vacancy.getName());
            ps.setString(2, vacancy.getDescription());
            ps.setString(3, vacancy.getLink());
            ps.setTimestamp(4, convertToTimestamp(vacancy.getCreated()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.error("DB update error", e);
            throw new RuntimeException("DB error");
        }
    }

    @Override
    public boolean delete(String id) {
        final String DELETE_VALUE = String.format("DELETE FROM %s WHERE %s = ?;", this.db.table, this.db.itemId);
        try (PreparedStatement ps = this.connection.prepareStatement(DELETE_VALUE)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.error("DB delete error", e);
            throw new RuntimeException("DB error");
        }
    }

    @Override
    public void deleteAll() {
        final String DELETE_ALL = String.format("TRUNCATE TABLE %s", this.db.table);
        try (Statement st = this.connection.createStatement()) {
            st.executeUpdate(DELETE_ALL);
        } catch (SQLException e) {
            LOG.error("DB deleteAll error ", e);
            throw new RuntimeException("DB error");
        }
    }

    @Override
    public int deleteByPeriod(LocalDateTime fromDate, LocalDateTime toDate) {
        final String DELETE_BY_PERIOD = String.format(
                "DELETE FROM %s WHERE %s >= ? AND %s <= ?;",
                this.db.table,
                this.db.itemCreated,
                this.db.itemCreated);
        try (PreparedStatement ps = this.connection.prepareStatement(DELETE_BY_PERIOD)) {
            ps.setTimestamp(1, convertToTimestamp(fromDate));
            ps.setTimestamp(2, convertToTimestamp(toDate));
            return ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error("DB deleteByPeriod error", e);
            throw new RuntimeException("DB error");
        }
    }


    @Override
    public Optional<Vacancy> findById(String id) {
        final String SELECT = String.format("SELECT * FROM %s WHERE %s = ?;",
                this.db.table,
                this.db.itemId);
        try (PreparedStatement ps = this.connection.prepareStatement(SELECT)) {
            ps.setString(1, id);
            ps.executeUpdate();
            ResultSet resultSet = ps.getResultSet();
            return resultSet.next() ? Optional.of(extractVacancy(resultSet)) : Optional.empty();
        } catch (SQLException e) {
            LOG.error("DB findById error", e);
            throw new RuntimeException("DB error");
        }
    }


    @Override
    public Collection<Vacancy> findByName(final String name) {
        if (name.isBlank()) {
            return Collections.emptyList();
        }
        final String SELECT = String.format("SELECT * FROM %s WHERE %s = ?;",
                this.db.table,
                this.db.itemName);
        try (PreparedStatement ps = this.connection.prepareStatement(SELECT)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            Collection<Vacancy> result = new ArrayDeque<>();
            while (rs.next()) {
                result.add(extractVacancy(rs));
            }
            return result;
        } catch (SQLException e) {
            LOG.error("DB findByName error", e);
            throw new RuntimeException("DB error");
        }
    }

    @Override
    public Collection<Vacancy> findAll() {
        final String SELECT = String.format("SELECT * FROM %s", this.db.table);
        try (Statement st = this.connection.createStatement()) {
            ResultSet rs = st.executeQuery(SELECT);
            Collection<Vacancy> result = new ArrayDeque<>();
            while (rs.next()) {
                result.add(extractVacancy(rs));
            }
            return result;
        } catch (SQLException e) {
            LOG.error("DB findAll error", e);
            throw new RuntimeException("DB error");
        }
    }

    @Override
    public Collection<Vacancy> findByPeriod(LocalDateTime fromDate, LocalDateTime toDate) {
        final String SELECT = String.format("SELECT * FROM %s WHERE %s >= ? AND %s <= ?;",
                this.db.table,
                this.db.itemCreated,
                this.db.itemCreated);
        try (PreparedStatement ps = this.connection.prepareStatement(SELECT)) {
            ps.setTimestamp(1, convertToTimestamp(fromDate));
            ps.setTimestamp(2, convertToTimestamp(toDate));
            ResultSet rs = ps.executeQuery();
            Collection<Vacancy> result = new ArrayDeque<>();
            while (rs.next()) {
                result.add(extractVacancy(rs));
            }
            return result;
        } catch (SQLException e) {
            LOG.error("DB findByPeriod error", e);
            throw new RuntimeException("DB error");
        }
    }

    @Override
    public Collection<Vacancy> findRecent(int number) {
        if (number < 0) {
            throw new IllegalArgumentException(String.format("Incorrect value %s", number));
        }
        if (number == 0) {
            return Collections.emptyList();
        }
        final String SELECT = String.format("SELECT * FROM %s ORDER BY %s DESC LIMIT ?;",
                this.db.table,
                this.db.itemCreated);
        try (PreparedStatement ps = this.connection.prepareStatement(SELECT)) {
            ps.setInt(1, number);
            Collection<Vacancy> result = new ArrayDeque<>();
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(extractVacancy(rs));
            }
            return result;
        } catch (SQLException e) {
            LOG.error("DB findRecent error", e);
            throw new RuntimeException("DB error");
        }
    }

    private Vacancy extractVacancy(final ResultSet resultSet) throws SQLException {
        return Vacancy.newBuilder()
                .setId(resultSet.getString(this.db.itemId))
                .setName(resultSet.getString(this.db.itemName))
                .setDescription(resultSet.getString(this.db.itemDesc))
                .setLink(resultSet.getString(this.db.itemLink))
                .setCreated(convertToLocalDateTime(resultSet.getTimestamp(this.db.itemCreated)))
                .build();
    }

    @Override
    public void close() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
            }
        } catch (SQLException e) {
            LOG.error("Error closing DB connection", e);
            throw new RuntimeException("DB error");
        }
    }

    private final class DbProperties {
        private final String driver = config.getString("jdbc.driver");
        private final int attemptsToConnect = config.getInt("jdbc.attempts");
        private final String dbName = config.getString("db.name");
        private final String url = config.getString("jdbc.url");
        private final String user = config.getString("jdbc.username");
        private final String pass = config.getString("jdbc.password");
        private final String table = config.getString("db.table.name");
        private final String itemId = config.getString("db.table.item_id");
        private final String itemName = config.getString("db.table.item_name");
        private final String itemDesc = config.getString("db.table.item_desc");
        private final String itemLink = config.getString("db.table.item_link");
        private final String itemCreated = config.getString("db.table.item_date");
    }
}
