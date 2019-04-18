package ru.job4j.exam.jobparser;


import java.io.File;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

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
        this.db = new DbProperties();
/*         this.connection = getConnection();
        checkDBStructure();*/

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
            checkDBStructure();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return this.connection != null;
    }

    private Connection getConnection() {
        final File dbFile = new File(".");
        loadDbDriver();
        Connection connect = null;
        final int timeout = 50;
        try {
            connect = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/vacancies_db; create=true", this.db.user, this.db.pass
    /*                    String.format("%s%s",
                                this.db.url,
                                //dbFile.getAbsolutePath(),
                                // File.pathSeparator,
                                this.db.dbName),
                        this.db.user,
                        this.db.pass*/
            );
        } catch (SQLException e) {
            e.printStackTrace(); //TODO logger
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
        // final String CREATE_DB = String.format("CREATE DATABASE %s", this.db.dbName);
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
        try (Statement st = this.connection.createStatement()) {
            // st.executeQuery(CREATE_DB);
            st.execute(CREATE_TABLE_IF_NOT_EXISTS);
        } catch (SQLException e) {
            e.printStackTrace(); //TODO logger
        }
      /*
        try (PreparedStatement ps = this.connection.prepareStatement(CREATE_TABLE_IF_NOT_EXISTS)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); //TODO logger
        }*/
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

    public int[] addAll(final Collection<Vacancy> vacancies) {
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

    public Collection<Vacancy> findAll() {
        final String SELECT_ALL = String.format("SELECT * FROM %s", this.db.table);
        Collection<Vacancy> result = new ArrayDeque<>();
        try (Statement st = this.connection.createStatement()) {
            ResultSet rs = st.executeQuery(SELECT_ALL);
            while (rs.next()) {
                result.add(createItem(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();  //TODO logger
        }
        return result; //TODO realize
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

    public List<Vacancy> findByDate(final Timestamp date) {
        validate(date);
        checkConnection();
        final String SELECT_BY_DATE = String.format("SELECT * FROM %s WHERE %s = %s;",
                this.db.table,
                this.db.itemDate,
                date
        );
        List<Vacancy> result = new ArrayList<>();
        try (PreparedStatement ps = this.connection.prepareStatement(SELECT_BY_DATE)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(createItem(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();  //TODO logger
        }
        return result.isEmpty() ? Collections.emptyList() : result;
    }

    public List<Vacancy> findByPeriod(final Timestamp fromDate, final Timestamp toDate) {
        validate(fromDate);
        validate(toDate);
        final String SELECT_BY_PERIOD = String.format("SELECT * FROM %s WHERE %s BETWEEN (%s AND %s);",
                this.db.table,
                this.db.itemDate,
                fromDate,
                toDate
        );
        List<Vacancy> result = new ArrayList<>();
        try (PreparedStatement ps = this.connection.prepareStatement(SELECT_BY_PERIOD)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(createItem(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();  //TODO logger
        }
        return result.isEmpty() ? Collections.emptyList() : result;
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
        checkConnection();
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

    public int deleteOlderThan(final Timestamp date) {
        validate(date);
        checkConnection();
        final String DELETE_OLDEST = String.format("DELETE FROM %s WHERE %s > %s;",
                this.db.table,
                this.db.itemDate,
                date
        );
        int result = 0;
        try (PreparedStatement ps = this.connection.prepareStatement(DELETE_OLDEST)) {
            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); //TODO logger
        }
        return result;
    }

    public int deleteByDate(final Timestamp date) {
        validate(date);
        checkConnection();
        final String DELETE_BY_DATE = String.format("DELETE FROM %s WHERE %s = %s;",
                this.db.table,
                this.db.itemDate,
                date
        );
        int result = 0;
        try (PreparedStatement ps = this.connection.prepareStatement(DELETE_BY_DATE)) {
            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); //TODO logger
        }
        return result;
    }

    public int deleteByPeriod(final Timestamp fromDate, final Timestamp toDate) {
        validate(fromDate);
        validate(toDate);
        checkConnection();
        final String DELETE_BY_PERIOD = String.format("DELETE FROM %s WHERE %s BETWEEN (%s AND %s);",
                this.db.table,
                this.db.itemDate,
                fromDate,
                toDate
        );
        int result = 0;
        try (PreparedStatement ps = this.connection.prepareStatement(DELETE_BY_PERIOD)) {
            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); //TODO logger
        }
        return result;
    }

    public int deleteAll() {
        final String DELETE_ALL = String.format("TRUNCATE TABLE %s", this.db.table);
        int result = 0;
        try (Statement st = this.connection.createStatement()) {
            result = st.executeUpdate(DELETE_ALL);
        } catch (SQLException e) {
            e.printStackTrace(); //TODO logger
        }
        return result;
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
