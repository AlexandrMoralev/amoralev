package ru.job4j.servlet;

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.controllers.Config;
import ru.job4j.crudservlet.Address;
import ru.job4j.crudservlet.Store;
import ru.job4j.crudservlet.User;
import ru.job4j.filtersecurity.Role;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * DBStore - persistence layout
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@ThreadSafe
public enum DBStore implements Store<User> {
    INSTANCE;

    private static final Logger LOG = LogManager.getLogger(DBStore.class);

    private static final String INSERT_INTO_USERS = "INSERT INTO users(name,login,created,pwd,role_desc,country,city) VALUES(?,?,?,?,?,?,?) RETURNING id;";
    private static final String UPDATE_USERS = "UPDATE users SET name = ?, login = ?, created = ?, pwd = ?, role_desc = ?, country = ?, city = ? WHERE id = ? ;";
    private static final String DELETE_FROM_USERS = "DELETE FROM users WHERE id = ? ;";
    private static final String SELECT_ALL_USERS = "SELECT * FROM users ;";
    private static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id = ? ;";
    private static final String SELECT_BY_LOGIN = "SELECT * FROM users WHERE login = ? ;";
    private static final String SELECT_BY_COUNTRY = "SELECT * FROM users WHERE country = ? ;";
    private static final String SELECT_BY_CITY = "SELECT * FROM users WHERE city = ? ;";
    private static final String CHECK_CREDENTIAL = "SELECT * FROM users WHERE login = ? AND pwd = ?;";

    private BasicDataSource source;
    private Config config;

    DBStore() {
        config = new Config();
        source = new BasicDataSource();
        initSource();
        initDB();
    }

    private void initSource() {
        source.setDriverClassName(config.getString("db.driver"));
        source.setUrl(config.getString("db.connection.url") + config.getString("db.name"));
        source.setUsername(config.getString("db.user"));
        source.setPassword(config.getString("db.pwd"));
        source.setMinIdle(config.getInt("db.idle.min"));
        source.setTimeBetweenEvictionRunsMillis(Long.valueOf(config.getInt("db.time.between.evictions")));
        source.setMaxIdle(config.getInt("db.idle.max"));
        source.setMaxOpenPreparedStatements(config.getInt("db.max.open.prepared.statements"));
    }

    private void initDB() {
        try (Connection connection = DriverManager.getConnection(config.getString("db.connection.url"), config.getString("db.user"), config.getString("db.pwd"));
             Statement st = connection.createStatement()
        ) {
            String checkDatabaseExists = String.format("SELECT EXISTS(SELECT * FROM pg_database WHERE datname = '%s');", config.getString("db.name"));
            ResultSet rs = st.executeQuery(checkDatabaseExists);
            if (rs.next()) {
                if (!rs.getBoolean(1)) {
                    String createDatabase = String.format("CREATE DATABASE %s;", config.getString("db.name"));
                    st.execute(createDatabase);
                }
            }
        } catch (SQLException e) {
            LOG.error("DB init error", e);
        }
        try (Connection connection = source.getConnection();
             Statement st = connection.createStatement()
        ) {
            st.execute("CREATE TABLE IF NOT EXISTS users(" // TODO normalize user_address relation
                    + "id SERIAL PRIMARY KEY NOT NULL,"
                    + " name VARCHAR(128) NOT NULL,"
                    + " login VARCHAR(256) NOT NULL,"
                    + " created VARCHAR(64),"
                    + " pwd VARCHAR(128),"
                    + " role_desc VARCHAR(64) NOT NULL,"
                    + " country VARCHAR(256) NOT NULL,"
                    + " city VARCHAR(256) NOT NULL,"
                    + "CONSTRAINT unq_login UNIQUE (login),"
                    + "CONSTRAINT unq_country UNIQUE (country),"
                    + "CONSTRAINT unq_city UNIQUE (city)"
                    + ");");
            st.execute("TRUNCATE TABLE users;");
            addRootUser(connection);
        } catch (SQLException e) {
            LOG.error("DB init error", e);
        }
    }

    private void addRootUser(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(INSERT_INTO_USERS);
        setQueryParameters(
                User.newBuilder()
                        .setId(0)
                        .setName("root")
                        .setLogin("root@root.ru")
                        .setCreated(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()))
                        .setPassword("root")
                        .setRole(Role.ROOT)
                        .setAddress(Address.newBuilder().setCountry("Russia").setCity("Spb").build())
                        .build(),
                ps);
        ps.execute();
    }

    @Override
    public Optional<Integer> add(User user) {
        try (Connection connection = source.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(INSERT_INTO_USERS);
            setQueryParameters(user, ps);
            if (ps.executeUpdate() > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                return Optional.of(rs.getInt(1));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            LOG.error("DB error", e);
            throw new RuntimeException("DB insert error");
        }
    }

    @Override
    public boolean update(User user) {
        boolean isUpdated = false;
        try (Connection connection = source.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(UPDATE_USERS);
            setQueryParameters(user, ps);
            ps.setInt(8, user.getId());
            int result = ps.executeUpdate();
            if (result == 1) {
                isUpdated = true;
            } else if (result != 0) {
                throw new SQLException(result + " users updated");
            }
        } catch (SQLException e) {
            LOG.error("DB error", e);
            throw new RuntimeException("DB update error");
        }
        return isUpdated;
    }

    private void setQueryParameters(User user, PreparedStatement ps) throws SQLException {
        ps.setString(1, user.getName());
        ps.setString(2, user.getLogin());
        ps.setString(3, user.getCreated());
        ps.setString(4, user.getPassword());
        ps.setString(5, user.getRole().getDescription());
        ps.setString(6, user.getAddress().getCountry());
        ps.setString(7, user.getAddress().getCity());
    }

    @Override
    public void delete(int userId) {
        try (Connection connection = source.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(DELETE_FROM_USERS);
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error("DB error", e);
            throw new RuntimeException("DB error deleting user");
        }
    }

    @Override
    public Collection<User> findAll() {
        return getUsers(SELECT_ALL_USERS, "DB findAll error");
    }

    @Override
    public Optional<User> findById(int id) {
        try (Connection connection = source.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(SELECT_USER_BY_ID);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(extractData(rs));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            LOG.error("DB error", e);
            throw new RuntimeException("DB findById error");
        }
    }

    @Override
    public Optional<User> findByLogin(String login) {
        try (Connection connection = source.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(SELECT_BY_LOGIN);
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(extractData(rs));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            LOG.error("DB error", e);
            throw new RuntimeException("DB findByLogin error");
        }
    }

    @Override
    public Collection<User> findByCountry(String country) {
        return getUsers(SELECT_BY_COUNTRY, "DB findByCountry error");
    }

    @Override
    public Collection<User> findByCity(String city) {
        return getUsers(SELECT_BY_CITY, "DB findByCity error");
    }

    @Override
    public boolean isCredential(String login, String password) {
        try (Connection connection = source.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(CHECK_CREDENTIAL);
            ps.setString(1, login);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                users.add(extractData(rs));
            }
            if (users.size() > 1) {
                throw new SQLException(users.size() + " has the same credentials");
            }
            return users.size() == 1;
        } catch (SQLException e) {
            LOG.error("DB error", e);
            throw new RuntimeException("DB isCredential error");
        }
    }

    private Collection<User> getUsers(String query, String errMsg) {
        try (Connection connection = source.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            Collection<User> allUsers = new ArrayDeque<>();
            while (rs.next()) {
                allUsers.add(extractData(rs));
            }
            return allUsers;
        } catch (SQLException e) {
            LOG.error("DB error", e);
            throw new RuntimeException(errMsg);
        }
    }

    private User extractData(ResultSet resultSet) throws SQLException {
        return User.newBuilder()
                .setId(resultSet.getInt(1))
                .setName(resultSet.getString(2))
                .setLogin(resultSet.getString(3))
                .setCreated(resultSet.getString(4))
                .setPassword(resultSet.getString(5))
                .setRole(Role.valueOf(resultSet.getString(6)))
                .setAddress(Address.newBuilder()
                        .setCountry(resultSet.getString(7))
                        .setCity(resultSet.getString(8)).build())
                .build();
    }
}
