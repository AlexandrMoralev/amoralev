package ru.job4j.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.tracker.ITracker;
import ru.job4j.tracker.Item;

import java.io.InputStream;
import java.sql.*;
import java.util.*;

/**
 * TrackerSQL
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class TrackerSQL implements ITracker, AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(TrackerSQL.class);
    private static final String tableName = "items";
    private Connection connection;

    public boolean init() {
        try (InputStream in = TrackerSQL.class
                .getClassLoader()
                .getResourceAsStream("app.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            this.connection = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
            checkTable();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new IllegalStateException(e);
        }
        return this.connection != null;
    }

    private void checkTable() {
        String ls = System.lineSeparator();
        try (PreparedStatement ps = this.connection
                .prepareStatement(new StringBuilder().append("CREATE TABLE IF NOT EXISTS ")
                        .append(TrackerSQL.tableName + " (").append(ls)
                        .append(" item_id BIGINT PRIMARY KEY NOT NULL,").append(ls)
                        .append(" name VARCHAR(30) NOT NULL,").append(ls)
                        .append(" description VARCHAR(100) NOT NULL,").append(ls)
                        .append(" created BIGINT NOT NULL,").append(ls)
                        .append(" comments VARCHAR(100)[] NOT NULL").append(" );")
                        .toString()
                )
        ) {
            ps.execute();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public Item add(final Item item) {
        validateArg(item);
        try (PreparedStatement ps = connection
                .prepareStatement(
                        String.format("INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?)",
                                TrackerSQL.tableName, "item_id", "name", "description", "created", "comments")
                )
        ) {
            ps.setLong(1, Long.parseLong(item.getId()));
            ps.setString(2, item.getName());
            ps.setString(3, item.getDescription());
            ps.setLong(4, item.getCreated());
            Array array = connection.createArrayOf("VARCHAR", item.getComments());
            ps.setArray(5, array);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return item;
    }

    @Override
    public void replace(final String id, final Item item) {
        validateArg(id);
        validateArg(item);
        try (PreparedStatement ps = connection
                .prepareStatement(
                        String.format("UPDATE %s SET %s = ?, %s = ?, %s = ? WHERE %s = ?;",
                                TrackerSQL.tableName, "name", "description", "comments", "item_id")
                )
        ) {
            ps.setString(1, item.getName());
            ps.setString(2, item.getDescription());
            Array array = connection.createArrayOf("VARCHAR", item.getComments());
            ps.setArray(3, array);
            ps.setLong(4, Long.parseLong(item.getId()));
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void delete(final String id) {
        validateArg(id);
        try (PreparedStatement ps = connection
                .prepareStatement(
                        String.format("DELETE * FROM %s WHERE %s = ?",
                                TrackerSQL.tableName, "item_id")
                )
        ) {
            ps.setLong(1, Long.parseLong(id));
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public List<Item> findAll() {
        List<Item> result = new ArrayList<>();
        try (PreparedStatement ps = connection
                .prepareStatement(
                        String.format("SELECT %s, %s, %s FROM %s",
                                "name", "description", "comments", TrackerSQL.tableName)
                )
        ) {
            ResultSet rs = ps.executeQuery();
            result = convertResultSetToList(rs, result);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return result.isEmpty() ? Collections.EMPTY_LIST : result;
    }

    @Override
    public List<Item> findByName(final String key) {
        validateArg(key);
        List<Item> result = new ArrayList<>();
        try (PreparedStatement ps = connection
                .prepareStatement(
                        String.format("SELECT * FROM %s WHERE %s = ?;",
                                TrackerSQL.tableName, "name")
                )
        ) {
            ps.setString(1, key);
            ResultSet rs = ps.executeQuery();
            result = convertResultSetToList(rs, result);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return result.isEmpty() ? Collections.EMPTY_LIST : result;
    }

    @Override
    public Item findById(final String id) {
        validateArg(id);
        Item result = null;
        try (PreparedStatement ps = connection
                .prepareStatement(
                        String.format("SELECT * FROM %s WHERE %s = ?;", TrackerSQL.tableName, "item_id")
                )
        ) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            rs.next();
            if (rs.last()) {
                result = buildItem(rs);
            } else {
                throw new IllegalStateException("id collision in the db");
            }

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public void close() throws Exception {
        this.connection.close();
    }

    private void validateArg(Object o) {
        if (o == null) {
            throw new IllegalArgumentException();
        }
    }

    private List<Item> convertResultSetToList(ResultSet resultSet, List<Item> list) throws SQLException {
        while (resultSet.next()) {
            list.add(buildItem(resultSet));
        }
        return list;
    }

    private Item buildItem(ResultSet resultSet) throws SQLException {
        return new Item(
                Objects.toString(resultSet.getLong("item_id")),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getLong("created"),
                (String[]) resultSet.getArray("comments").getArray()
        );
    }
}