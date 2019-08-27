package ru.job4j.servlet;

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.crudservlet.Store;
import ru.job4j.crudservlet.User;

import java.sql.*;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Optional;

/**
 * MemoryStore - persistence layout
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@ThreadSafe
public enum DBStore implements Store<User> {
    INSTANCE;
    private final BasicDataSource source = new BasicDataSource();
    private static final String DB_DRIVER = "org.postgresql.Driver";
    private static final String DB_CONNECTION_URL = "jdbc:postgresql://localhost:5432/";
    private static final String DB_USER = "postgres";
    private static final String DB_PWD = "postgres";
    private static final String DB_EXISTS = "SELECT EXISTS(SELECT * FROM pg_database WHERE datname = 'users_app');";
    private static final String CREATE_DB = "CREATE DATABASE users_app;";
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS users(id SERIAL PRIMARY KEY, name VARCHAR(120), login VARCHAR(160), email VARCHAR(160), created VARCHAR(60))";
    private static final String INSERT_INTO_USERS = "INSERT INTO users(name,login,email,created) VALUES(?,?,?,?) RETURNING id;";
    private static final String UPDATE_USERS = "UPDATE users SET name = ?, login = ?, email = ?, created = ? WHERE id = ? ;";
    private static final String DELETE_FROM_USERS = "DELETE FROM users WHERE id = ? ;";
    private static final String SELECT_ALL = "SELECT * FROM users ;";
    private static final String SELECT_BY_ID = "SELECT * FROM users WHERE id = ? ;";
    private static final String SELECT_BY_LOGIN = "SELECT * FROM users WHERE login = ? ;";

    DBStore() {
        source.setDriverClassName(DB_DRIVER);
        source.setUrl(DB_CONNECTION_URL);
        source.setUsername(DB_USER);
        source.setPassword(DB_PWD);
        source.setMinIdle(5);
        source.setMaxIdle(10);
        source.setMaxOpenPreparedStatements(100);
        init();
    }

    private void init() {
        try (Connection connection = DriverManager.getConnection(DB_CONNECTION_URL, DB_USER, DB_PWD);
             Statement st = connection.createStatement()
        ) {
            ResultSet rs = st.executeQuery(DB_EXISTS);
            if (rs.next()) {
                if (!rs.getBoolean(1)) {
                    st.execute(CREATE_DB);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (Connection connection = source.getConnection();
             Statement st = connection.createStatement()
        ) {
            st.execute(CREATE_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Integer> add(User user) {
        Optional<Integer> userId = Optional.empty();
        try (Connection connection = source.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(INSERT_INTO_USERS);
            setQueryParameters(user, ps);
            if (ps.executeUpdate() > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                userId = Optional.of(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }

    @Override
    public boolean update(int id, User user) {
        boolean isUpdated = false;
        try (Connection connection = source.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(UPDATE_USERS);
            setQueryParameters(user, ps);
            ps.setInt(5, id);
            int result = ps.executeUpdate();
            if (result == 1) {
                isUpdated = true;
            } else if (result != 0) {
                throw new SQLException(result + " users updated");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isUpdated;
    }

    private void setQueryParameters(User user, PreparedStatement ps) throws SQLException {
        ps.setString(1, user.getName());
        ps.setString(2, user.getLogin());
        ps.setString(3, user.getEmail());
        ps.setString(4, user.getCreated());
    }

    @Override
    public void delete(int userId) {
        try (Connection connection = source.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(DELETE_FROM_USERS);
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Collection<User> findAll() {
        Collection<User> allUsers = new ArrayDeque<>();
        try (Connection connection = source.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(SELECT_ALL);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                allUsers.add(extractData(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allUsers;
    }

    @Override
    public Optional<User> findById(int id) {
        Optional<User> optionalUser = Optional.empty();
        try (Connection connection = source.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                optionalUser = Optional.of(extractData(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return optionalUser;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        Optional<User> optionalUser = Optional.empty();
        try (Connection connection = source.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(SELECT_BY_LOGIN);
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                optionalUser = Optional.of(extractData(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return optionalUser;
    }

    private User extractData(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getString(4),
                resultSet.getString(5)
        );
    }
}
