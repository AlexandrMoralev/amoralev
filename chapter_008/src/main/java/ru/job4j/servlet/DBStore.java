package ru.job4j.servlet;

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.crudservlet.Store;
import ru.job4j.crudservlet.User;

import java.sql.*;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

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
    private static final String DB_CONNECTION_URL = "jdbc:postgresql://localhost:5432/users_app";
    private static final String DB_USER = "postgres";
    private static final String DB_PWD = "postgres";
    private final AtomicInteger idCounter = new AtomicInteger(1);

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
        String dbExists = "SELECT EXISTS(SELECT * FROM pg_database WHERE datname = 'users_db');";
        String createDb = "CREATE DATABASE users_db;";
        String createTable = "CREATE TABLE IF NOT EXISTS users(id SERIAL PRIMARY KEY, name VARCHAR(120), login VARCHAR(160), email VARCHAR(160), created VARCHAR(60))";
        try (Connection connection = DriverManager.getConnection(DB_CONNECTION_URL, DB_USER, DB_PWD);
             Statement st = connection.createStatement()
        ) {
            ResultSet rs = st.executeQuery(dbExists);
            if (rs.next()) {
                if (!rs.getBoolean(1)) {
                    st.execute(createDb);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (Connection connection = source.getConnection();
             Statement st = connection.createStatement()
        ) {
            st.execute(createTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean add(User user) {
        String insertIntoUsers = "INSERT INTO users(id,name,login,email,created) VALUES(?,?,?,?,?);";
        boolean isAdded = false;
        try (Connection connection = source.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(insertIntoUsers);
            ps.setInt(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getLogin());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getCreated());
            if (ps.executeUpdate() > 0) {
                isAdded = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isAdded;
    }

    @Override
    public boolean update(int id, User user) {
        String updateUsers = "UPDATE users SET name = ?, login = ?, email = ?, created = ? WHERE id = ? ;";
        boolean isUpdated = false;
        try (Connection connection = source.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(updateUsers);
            ps.setString(1, user.getName());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getCreated());
            ps.setInt(5, id);
            int result = ps.executeUpdate();
            if (result == 1) {
                isUpdated = true;
            } else if (result != 0) {
                throw new SQLException(result + " user updated");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isUpdated;
    }

    @Override
    public void delete(int userId) {
        String deleteFromUsers = "DELETE FROM users WHERE id = ? ;";
        try (Connection connection = source.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(deleteFromUsers);
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Collection<User> findAll() {
        String selectAll = "SELECT * FROM users ;";
        Collection<User> allUsers = new ArrayDeque<>();
        try (Connection connection = source.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(selectAll);
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
        String selectById = "SELECT * FROM users WHERE id = ? ;";
        Optional<User> optionalUser = Optional.empty();
        try (Connection connection = source.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(selectById);
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
        String selectByLogin = "SELECT * FROM users WHERE login = ? ;";
        Optional<User> optionalUser = Optional.empty();
        try (Connection connection = source.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(selectByLogin);
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

    @Override
    public int nextIndex() {
        return this.idCounter.getAndIncrement();
    }

    private User extractData(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getString(4)
        );
    }
}
