package ru.job4j.jdbc;

import ru.job4j.tracker.Comment;
import ru.job4j.tracker.ITracker;
import ru.job4j.tracker.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * TrackerSQL
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class TrackerSQL implements ITracker, AutoCloseable {
    private static final Logger LOG = LoggerFactory.getLogger(TrackerSQL.class);
    private final Comment[] comments = new Comment[0];
    private Connection connection;

    /**
     * Method init - initializing of the TrackerSQL
     * loads config from a properties file, gets the DB connection using DriverManager,
     * if the db table doesn't exists a new one is created
     *
     * @return boolean true, if initialisation is successful,
     * false otherwise
     */
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
            checkTables();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new IllegalStateException(e);
        }
        return this.connection != null;
    }

    private void checkTables() {
        try (PreparedStatement ps = this.connection
                .prepareStatement(DBNaming.CREATE_TABLES)
        ) {
            ps.execute();
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public Item add(final Item item) {
        validateArg(item);
        int added = -1;
        try (PreparedStatement ps = connection
                .prepareStatement(DBNaming.ADD_TO_ITEMS, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, item.getName());
            ps.setString(2, item.getDescription());
            ps.setLong(3, item.getCreated());
            added = ps.executeUpdate();
            if (added == 1) {
                ResultSet generatedId = ps.getGeneratedKeys();
                generatedId.next();
                item.setId(generatedId.getString(DBNaming.ITEMS_ID));
            } else {
                throw new IllegalStateException();
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        if (added == 1) {
            try (PreparedStatement ps = connection
                    .prepareStatement(DBNaming.ADD_TO_COMMENTARIES, Statement.RETURN_GENERATED_KEYS)
            ) {
                for (Comment comment : item.getComments()) {
                    ps.setString(1, comment.getComment());
                    ps.setString(2, item.getId());
                    int updated = ps.executeUpdate();
                    if (updated == 1) {
                        ResultSet generatedId = ps.getGeneratedKeys();
                        generatedId.next();
                        comment.setId(generatedId.getInt(DBNaming.COMMENTARIES_COMMENT_ID));
                    }
                }
            } catch (SQLException e) {
                LOG.error(e.getMessage(), e);
            }
        }
        return item;
    }

    @Override
    public void replace(final String id, final Item item) {
        validateArg(id);
        validateArg(item);
        int replaced = -1;
        try (PreparedStatement ps = connection
                .prepareStatement(DBNaming.REPLACE_FROM_ITEMS)
        ) {
            ps.setString(1, item.getName());
            ps.setString(2, item.getDescription());
            ps.setLong(3, Long.parseLong(item.getId()));
            replaced = ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        if (replaced == 1) {
            Comment[] comments = item.getComments();
            for (Comment comment : comments) {
                try (PreparedStatement ps = connection
                        .prepareStatement(DBNaming.REPLACE_FROM_COMMENTARIES)
                ) {
                    ps.setString(1, comment.getComment());
                    ps.setInt(2, comment.getId());
                    ps.setLong(3, comment.getItemId());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
    }

    @Override
    public void delete(final String id) {
        validateArg(id);
        int deleted = -1;
        try (PreparedStatement ps = connection
                .prepareStatement(DBNaming.DELETE_FROM_ITEMS)
        ) {
            ps.setLong(1, Long.parseLong(id));
            deleted = ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        if (deleted == 1) {
            try (PreparedStatement ps = connection
                    .prepareStatement(DBNaming.DELETE_FROM_COMMENTARIES)
            ) {
                ps.setLong(1, Long.parseLong(id));
                ps.executeUpdate();
            } catch (SQLException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public List<Item> findAll() {
        List<Item> result = new ArrayList<>();
        try (PreparedStatement ps = connection
                .prepareStatement(DBNaming.FIND_ALL_FROM_ITEMS)
        ) {
            ResultSet rs = ps.executeQuery();
            result = convertResultSetToList(rs, result);
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        if (!result.isEmpty()) {
            for (Item item : result) {
                try (PreparedStatement ps = connection
                        .prepareStatement(DBNaming.FIND_ALL_FROM_COMMENTARIES)
                ) {
                    ps.setLong(1, Long.parseLong(item.getId()));
                    ResultSet resultSet = ps.executeQuery();
                    item.setComments(addCommentsToItem(resultSet));
                } catch (SQLException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
        return result.isEmpty() ? Collections.EMPTY_LIST : result;
    }

    @Override
    public List<Item> findByName(final String key) {
        validateArg(key);
        List<Item> result = new ArrayList<>();
        try (PreparedStatement ps = connection
                .prepareStatement(DBNaming.FIND_ITEM_BY_NAME_FROM_ITEMS)
        ) {
            ps.setString(1, key);
            ResultSet rs = ps.executeQuery();
            result = convertResultSetToList(rs, result);
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        if (!result.isEmpty()) {
            for (Item item : result) {
                try (PreparedStatement ps = connection
                        .prepareStatement(DBNaming.FIND_ITEM_BY_NAME_FROM_COMMENTARIES)
                ) {
                    ps.setLong(1, Long.parseLong(item.getId()));
                    ResultSet resultSet = ps.executeQuery();
                    item.setComments(addCommentsToItem(resultSet));
                } catch (SQLException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
        return result.isEmpty() ? Collections.EMPTY_LIST : result;
    }

    @Override
    public Item findById(final String id) {
        validateArg(id);
        Item result = null;
        ResultSet itemsRS;
        ResultSet commentsRS;
        try (PreparedStatement ps = connection
                .prepareStatement(DBNaming.FIND_ITEM_BY_ID_FROM_ITEMS)
        ) {
            ps.setString(1, id);
            itemsRS = ps.executeQuery();
            result = buildItem(itemsRS);
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        try (PreparedStatement ps = connection
                .prepareStatement(DBNaming.FIND_ITEM_BY_ID_FROM_COMMENTARIES)
        ) {
            ps.setLong(1, Long.parseLong(id));
            commentsRS = ps.executeQuery();
            result.setComments(addCommentsToItem(commentsRS));
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
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

    private Item buildItem(ResultSet itemResultSet) throws SQLException {
        return new Item(itemResultSet.getString(DBNaming.ITEMS_ID),
                itemResultSet.getString(DBNaming.ITEMS_NAME),
                itemResultSet.getString(DBNaming.ITEMS_DESCRIPTION),
                itemResultSet.getLong(DBNaming.ITEMS_CREATED),
                this.comments
        );
    }

    private Comment[] addCommentsToItem(ResultSet resultSet) throws SQLException {
        Comment[] comments = new Comment[Item.getCommentsSize()];
        if (resultSet != null) {
            int counter = 0;
            while (resultSet.next()) {
                comments[counter++].setComment(resultSet.getString(DBNaming.COMMENTARIES_COMMENT));
            }
        }
        return comments;
    }

    private static class DBNaming {
        private static final String MAIN_TABLE_NAME = "items";
        private static final String SECONDARY_TABLE_NAME = "commentaries";
        private static final String ITEMS_ID = "item_id";
        private static final String ITEMS_NAME = "name";
        private static final String ITEMS_DESCRIPTION = "description";
        private static final String ITEMS_CREATED = "created";
        private static final String COMMENTARIES_COMMENT_ID = "comment_id";
        private static final String COMMENTARIES_COMMENT = "comment";
        private static final String COMMENTARIES_ITEM_ID = "item_id";
        private static final String LINE_SEPARATOR = System.lineSeparator();
        private static final String CREATE_TABLES =
                new StringBuilder().append("CREATE TABLE IF NOT EXISTS ")
                        .append(MAIN_TABLE_NAME + " (").append(LINE_SEPARATOR)
                        .append(ITEMS_ID).append(" SERIAL PRIMARY KEY,").append(LINE_SEPARATOR)
                        .append(ITEMS_NAME).append(" VARCHAR(30) NOT NULL,").append(LINE_SEPARATOR)
                        .append(ITEMS_DESCRIPTION).append(" VARCHAR(100) NOT NULL,").append(LINE_SEPARATOR)
                        .append(ITEMS_CREATED).append(" BIGINT NOT NULL").append(" );").append(LINE_SEPARATOR)
                        .append("CREATE TABLE IF NOT EXISTS ")
                        .append(SECONDARY_TABLE_NAME + " (").append(LINE_SEPARATOR)
                        .append(COMMENTARIES_COMMENT_ID).append(" SERIAL PRIMARY KEY,").append(LINE_SEPARATOR)
                        .append(COMMENTARIES_COMMENT).append(" VARCHAR(100) NOT NULL,").append(LINE_SEPARATOR)
                        .append(COMMENTARIES_ITEM_ID).append(String.format(" SERIAL REFERENCES %s(%s) NOT NULL", MAIN_TABLE_NAME, ITEMS_ID))
                        .append(" );").append(LINE_SEPARATOR).toString();

        private static final String ADD_TO_ITEMS =
                String.format("INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?);",
                        MAIN_TABLE_NAME, ITEMS_NAME, ITEMS_DESCRIPTION, ITEMS_CREATED
                );
        private static final String ADD_TO_COMMENTARIES =
                String.format("INSERT INTO %s (%s, %s) VALUES (?, ?);",
                        SECONDARY_TABLE_NAME, COMMENTARIES_COMMENT, COMMENTARIES_ITEM_ID
                );
        private static final String REPLACE_FROM_ITEMS =
                String.format("UPDATE %s SET %s = ?, %s = ?, %s = ? WHERE %s = ?;",
                        MAIN_TABLE_NAME, ITEMS_NAME, ITEMS_DESCRIPTION, ITEMS_CREATED, ITEMS_ID
                );
        private static final String REPLACE_FROM_COMMENTARIES =
                String.format("UPDATE %s SET %s = ? WHERE %s = ? AND %s = ?;",
                        SECONDARY_TABLE_NAME, COMMENTARIES_COMMENT, COMMENTARIES_COMMENT_ID, COMMENTARIES_ITEM_ID
                );
        private static final String DELETE_FROM_ITEMS =
                String.format("DELETE * FROM %s WHERE %s = ?;",
                        MAIN_TABLE_NAME, ITEMS_ID
                );
        private static final String DELETE_FROM_COMMENTARIES =
                String.format("DELETE * FROM %s, WHERE %s = ?;",
                        SECONDARY_TABLE_NAME, COMMENTARIES_ITEM_ID
                );
        private static final String FIND_ALL_FROM_ITEMS =
                String.format("SELECT %s, %s, %s, %s FROM %s;",
                        ITEMS_ID, ITEMS_NAME, ITEMS_DESCRIPTION, ITEMS_CREATED, MAIN_TABLE_NAME
                );
        private static final String FIND_ALL_FROM_COMMENTARIES =
                String.format("SELECT %s FROM %s WHERE %s = ?;",
                        COMMENTARIES_COMMENT, SECONDARY_TABLE_NAME, COMMENTARIES_ITEM_ID
                );
        private static final String FIND_ITEM_BY_NAME_FROM_ITEMS =
                String.format("SELECT * FROM %s WHERE %s = ?;",
                        MAIN_TABLE_NAME, ITEMS_NAME
                );
        private static final String FIND_ITEM_BY_NAME_FROM_COMMENTARIES =
                String.format("SELECT %s FROM %s WHERE %s = ?;",
                        COMMENTARIES_COMMENT, SECONDARY_TABLE_NAME, COMMENTARIES_ITEM_ID
                );
        private static final String FIND_ITEM_BY_ID_FROM_ITEMS =
                String.format("SELECT * FROM %s WHERE %s = ?;",
                        MAIN_TABLE_NAME, ITEMS_NAME
                );
        private static final String FIND_ITEM_BY_ID_FROM_COMMENTARIES =
                String.format("SELECT %s FROM %s WHERE %s = ?;",
                        COMMENTARIES_COMMENT, SECONDARY_TABLE_NAME, COMMENTARIES_ITEM_ID
                );
    }
}
