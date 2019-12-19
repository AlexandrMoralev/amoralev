package ru.job4j.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.tracker.Comment;
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
    private static final Logger LOG = LogManager.getLogger(TrackerSQL.class);
    private final Comment[] comments = new Comment[0];
    private final Map<Class<?>, TripleConsumerEx<Integer, PreparedStatement, Object>> dispatch = new HashMap<>();
    private Connection connection;

    /**
     * Constructs TrackerSQL instance, prepares for DB using and
     */
    public TrackerSQL() {
        this.init();
        this.dispatch.put(String.class, (index, ps, value) -> ps.setString(index, (String) value));
        this.dispatch.put(Long.class, (index, ps, value) -> ps.setLong(index, (Long) value));
    }

    /**
     * Method init - loads config from a properties file, gets the DB connection using DriverManager,
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
            LOG.error("Initialisation error ", e);
            throw new IllegalStateException(e);
        }
        return this.connection != null;
    }

    private void checkTables() {
        this.db(DBNaming.CREATE_TABLES,
                List.of(),
                (ConsumerEx<PreparedStatement>) PreparedStatement::execute
        );
    }

    private <T> void forIndex(List<T> list,
                              BiConsumerEx<Integer, T> consumer) throws Exception {
        for (int index = 0; index != list.size(); index++) {
            consumer.accept(index, list.get(index));
        }
    }

    private <R> Optional<R> db(String sql,
                               List<Object> params,
                               FunctionEx<PreparedStatement, R> fun,
                               int key
    ) {
        Optional<R> result = Optional.empty();
        try (PreparedStatement pr = this.connection
                .prepareStatement(sql, key)
        ) {
            this.forIndex(params, (index, value) -> dispatch
                    .get(value.getClass())
                    .accept(index + 1, pr, value)
            );
            result = Optional.of(fun.apply(pr));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    // overloaded without key
    private <R> Optional<R> db(String sql,
                               List<Object> params,
                               FunctionEx<PreparedStatement, R> fun
    ) {
        return this.db(sql, params, fun, Statement.NO_GENERATED_KEYS);
    }

    // the same DB without return value
    private <E> void db(String sql,
                        List<Object> params,
                        ConsumerEx<PreparedStatement> fun,
                        int key
    ) {
        this.db(sql, params,
                ps -> {
                    ex(() -> fun.accept(ps));
                    return Optional.empty();
                }, key
        );
    }

    // overloaded DB without return value
    private <R> void db(String sql,
                        List<Object> params,
                        ConsumerEx<PreparedStatement> fun
    ) {
        this.db(sql, params, fun, Statement.NO_GENERATED_KEYS);
    }

    // wrapper to catch exceptions
    private void ex(UnaryEx unary) {
        try {
            unary.action();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public Item add(final Item item) {
        validateArg(item);
        this.db(
                DBNaming.ADD_TO_ITEMS,
                List.of(item.getName(), item.getDescription(), item.getCreated()),
                psi -> {
                    if (psi.executeUpdate() == 1) {
                        ResultSet itemId = psi.getGeneratedKeys();
                        itemId.next();
                        String id = itemId.getString(DBNaming.ITEMS_ID);
                        item.setId(id);
                        for (Comment comment : item.getComments()) {
                            this.db(
                                    DBNaming.ADD_TO_COMMENTARIES,
                                    List.of(comment.getComment(), id),
                                    psc -> {
                                        if (psc.executeUpdate() == 1) {
                                            ResultSet commentId = psc.getGeneratedKeys();
                                            commentId.next();
                                            comment.setId(commentId.getInt(DBNaming.COMMENTARIES_COMMENT_ID));
                                        }
                                    },
                                    Statement.RETURN_GENERATED_KEYS
                            );
                        }
                    }
                },
                Statement.RETURN_GENERATED_KEYS
        );
        return item;
    }

    @Override
    public void replace(final String id, final Item item) {
        validateArg(id);
        validateArg(item);
        this.db(
                DBNaming.REPLACE_FROM_ITEMS,
                List.of(item.getName(), item.getDescription(), Long.parseLong(item.getId())),
                psi -> {
                    if (psi.executeUpdate() == 1) {
                        for (Comment comment : item.getComments()) {
                            this.db(
                                    DBNaming.REPLACE_FROM_COMMENTARIES,
                                    List.of(comment.getComment(), comment.getId(), comment.getItemId()),
                                    (ConsumerEx<PreparedStatement>) PreparedStatement::executeUpdate
                            );
                        }
                    }
                }
        );
    }

    @Override
    public void delete(final String id) {
        validateArg(id);
        final Long longId = Long.parseLong(id);
        this.db(
                DBNaming.DELETE_FROM_ITEMS,
                List.of(longId),
                psi -> {
                    if (psi.executeUpdate() == 1) {
                        this.db(DBNaming.DELETE_FROM_COMMENTARIES,
                                List.of(longId),
                                (ConsumerEx<PreparedStatement>) PreparedStatement::executeUpdate
                        );
                    }
                }
        );

    }

    @Override
    public List<Item> findAll() {
        final List<Item> result = new ArrayList<>();
        this.db(
                DBNaming.FIND_ALL_FROM_ITEMS,
                List.of(),
                psi -> {
                    ResultSet rsi = psi.executeQuery();
                    convertResultSetToList(rsi, result);
                    if (!result.isEmpty()) {
                        for (Item item : result) {
                            this.db(
                                    DBNaming.FIND_ALL_FROM_COMMENTARIES,
                                    List.of(Long.parseLong(item.getId())),
                                    psc -> {
                                        ResultSet rsc = psc.executeQuery();
                                        item.setComments(addCommentsToItem(rsc));
                                    }
                            );
                        }
                    }
                }
        );
        return result.isEmpty() ? Collections.emptyList() : result;
    }

    @Override
    public List<Item> findByName(final String key) {
        validateArg(key);
        List<Item> result = new ArrayList<>();
        this.db(
                DBNaming.FIND_ITEM_BY_NAME_FROM_ITEMS,
                List.of(key),
                psi -> {
                    ResultSet rsi = psi.executeQuery();
                    convertResultSetToList(rsi, result);
                    if (!result.isEmpty()) {
                        for (Item item : result) {
                            this.db(
                                    DBNaming.FIND_ITEM_BY_NAME_FROM_COMMENTARIES,
                                    List.of(Long.parseLong(item.getId())),
                                    psc -> {
                                        ResultSet rsc = psc.executeQuery();
                                        item.setComments(addCommentsToItem(rsc));
                                    }
                            );
                        }
                    }
                }
        );
        return result.isEmpty() ? Collections.emptyList() : result;
    }

    @Override
    public Item findById(final String id) {
        validateArg(id);
        final Long longId = Long.parseLong(id);
        final Optional<Item> result = this.db(
                DBNaming.FIND_ITEM_BY_ID_FROM_ITEMS,
                List.of(longId),
                psi -> {
                    ResultSet rsi = psi.executeQuery();
                    return this.db(
                            DBNaming.FIND_ITEM_BY_ID_FROM_COMMENTARIES,
                            List.of(longId),
                            psc -> {
                                ResultSet rsc = psc.executeQuery();
                                Item rsl = buildItem(rsi);
                                rsl.setComments(addCommentsToItem(rsc));
                                return rsl;
                            }
                    );
                }
        ).get();
        return result.get();
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
