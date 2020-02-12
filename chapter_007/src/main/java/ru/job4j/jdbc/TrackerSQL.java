package ru.job4j.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.tracker.Comment;
import ru.job4j.tracker.ITracker;
import ru.job4j.tracker.Item;

import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
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
     * Constructs TrackerSQL instance
     */
    public TrackerSQL(Connection connection) {
        this.connection = connection;
        this.dispatch.put(String.class, (index, ps, value) -> ps.setString(index, (String) value));
        this.dispatch.put(Integer.class, (index, ps, value) -> ps.setInt(index, (Integer) value));
        this.dispatch.put(Timestamp.class, (index, ps, value) -> ps.setTimestamp(index, (Timestamp) value));
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
        } catch (Exception e) {
            LOG.error("Initialisation error ", e);
            throw new IllegalStateException(e);
        }
        return this.connection != null;
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
    public Integer add(final Item item) {
        validateArg(item);
        Integer[] result = new Integer[1];
        final Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        this.db(
                DBNaming.INSERT_INTO_ITEMS,
                List.of(item.getName(), item.getDescription(), now),
                psi -> {
                    if (psi.executeUpdate() == 1) {
                        ResultSet itemId = psi.getGeneratedKeys();
                        itemId.next();
                        Integer id = itemId.getInt(DBNaming.ITEMS_ID);
                        item.getComments()
                                .stream()
                                .forEach(comment -> this.db(
                                        DBNaming.INSERT_INTO_COMMENTS,
                                        List.of(comment.getComment(), id, now),
                                        psc -> {
                                            if (psc.executeUpdate() == 1) {
                                                result[0] = id;
                                            }
                                        },
                                        Statement.RETURN_GENERATED_KEYS
                                        )
                                );
                    }
                },
                Statement.RETURN_GENERATED_KEYS
        );
        return result[0];
    }

    @Override
    public void update(final Item item) {
        validateArg(item);
        this.db(
                DBNaming.UPDATE_ITEM,
                List.of(item.getName(), item.getDescription(), item.getId()),
                psi -> {
                    if (psi.executeUpdate() == 1) {
                        item.getComments()
                                .stream()
                                .forEach(comment -> this.db(
                                        DBNaming.UPDATE_COMMENT_BY_ITEMID,
                                        List.of(comment.getComment(), comment.getCommentId(), comment.getItemId()),
                                        (ConsumerEx<PreparedStatement>) PreparedStatement::executeUpdate
                                ));
                    }
                }
        );
    }

    @Override
    public void delete(final Integer id) {
        validateArg(id);
        this.db(
                DBNaming.DELETE_FROM_ITEMS,
                List.of(id),
                psi -> {
                    if (psi.executeUpdate() == 1) {
                        LOG.info("Item id: {} deleted", id);
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
                    convertResultSetToItems(rsi)
                            .stream()
                            .forEach(item -> this.db(
                                    DBNaming.FIND_COMMENTS_BY_ITEMID,
                                    List.of(item.getId()),
                                    psc -> {
                                        ResultSet rsc = psc.executeQuery();
                                        result.add(
                                                Item.newBuilder().of(item)
                                                        .setComments(extractComments(rsc))
                                                        .build()
                                        );
                                    }
                            ));
                }
        );
        return result.isEmpty() ? Collections.emptyList() : result;
    }

    @Override
    public List<Item> findByName(final String key) {
        validateArg(key);
        List<Item> result = new ArrayList<>();
        this.db(
                DBNaming.FIND_ITEMS_BY_NAME,
                List.of(key),
                psi -> {
                    ResultSet rsi = psi.executeQuery();
                    convertResultSetToItems(rsi)
                            .stream()
                            .forEach(item -> this.db(
                                    DBNaming.FIND_COMMENTS_BY_ITEMID,
                                    List.of(item.getId()),
                                    psc -> {
                                        ResultSet rsc = psc.executeQuery();
                                        result.add(
                                                Item.newBuilder().of(item)
                                                        .setComments(extractComments(rsc))
                                                        .build()
                                        );
                                    }
                            ));

                }
        );
        return result.isEmpty() ? Collections.emptyList() : result;
    }

    @Override
    public Optional<Item> findById(final Integer id) {
        validateArg(id);
        return this.db(
                DBNaming.FIND_ITEM_BY_ID,
                List.of(id),
                psi -> {
                    ResultSet rsi = psi.executeQuery();
                    return this.db(
                            DBNaming.FIND_COMMENTS_BY_ITEMID,
                            List.of(id),
                            psc -> {
                                ResultSet rsc = psc.executeQuery();
                                Collection<Comment> comments = extractComments(rsc);
                                return convertResultSetToItems(rsi)
                                        .stream()
                                        .map(item -> Item.newBuilder().of(item).setComments(comments).build())
                                        .findFirst()
                                        .orElse(null);
                            }
                    ).orElse(null);
                }
        );
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

    private Collection<Item> convertResultSetToItems(ResultSet resultSet) throws SQLException {
        List<Item> items = new ArrayList<>();
        while (resultSet.next()) {
            items.add(extractItemData(resultSet).build());
        }
        return items;
    }

    private Item.Builder extractItemData(ResultSet itemResultSet) throws SQLException {
        return Item.newBuilder()
                .setId(itemResultSet.getInt(DBNaming.ITEMS_ID))
                .setName(itemResultSet.getString(DBNaming.ITEMS_NAME))
                .setDescription(itemResultSet.getString(DBNaming.ITEMS_DESCRIPTION))
                .setCreated(itemResultSet.getTimestamp(DBNaming.ITEMS_CREATED).getTime());
    }

    private Collection<Comment> extractComments(ResultSet resultSet) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        while (resultSet.next()) {
            comments.add(
                    Comment.newBuilder()
                            .setCommentId(resultSet.getInt(DBNaming.COMMENTS_COMMENT_ID))
                            .setComment(resultSet.getString(DBNaming.COMMENTS_COMMENT))
                            .setItemId(resultSet.getInt(DBNaming.COMMENTS_ITEM_ID))
                            .setCreated(resultSet.getTimestamp(DBNaming.COMMENTS_CREATED).getTime())
                            .build()
            );
        }
        return comments;
    }

    private static class DBNaming {
        private static final String MAIN_TABLE_NAME = "items";
        private static final String SECONDARY_TABLE_NAME = "comments";
        private static final String ITEMS_ID = "item_id";
        private static final String ITEMS_NAME = "name";
        private static final String ITEMS_DESCRIPTION = "description";
        private static final String ITEMS_CREATED = "created";
        private static final String COMMENTS_COMMENT_ID = "comment_id";
        private static final String COMMENTS_COMMENT = "comment";
        private static final String COMMENTS_ITEM_ID = "items_id";
        private static final String COMMENTS_CREATED = "created";

        private static final String INSERT_INTO_ITEMS =
                String.format("INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?);",
                        MAIN_TABLE_NAME, ITEMS_NAME, ITEMS_DESCRIPTION, ITEMS_CREATED
                );
        private static final String INSERT_INTO_COMMENTS =
                String.format("INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?);",
                        SECONDARY_TABLE_NAME, COMMENTS_COMMENT, COMMENTS_ITEM_ID, COMMENTS_CREATED
                );
        private static final String UPDATE_ITEM =
                String.format("UPDATE %s SET %s = ?, %s = ? WHERE %s = ?;",
                        MAIN_TABLE_NAME, ITEMS_NAME, ITEMS_DESCRIPTION, ITEMS_ID
                );
        private static final String UPDATE_COMMENT_BY_ITEMID =
                String.format("UPDATE %s SET %s = ? WHERE %s = ? AND %s = ?;",
                        SECONDARY_TABLE_NAME, COMMENTS_COMMENT, COMMENTS_COMMENT_ID, COMMENTS_ITEM_ID
                );
        private static final String DELETE_FROM_ITEMS =
                String.format("DELETE FROM %s WHERE %s = ?;",
                        MAIN_TABLE_NAME, ITEMS_ID
                );
        private static final String FIND_ALL_FROM_ITEMS =
                String.format("SELECT * FROM %s;", MAIN_TABLE_NAME
                );
        private static final String FIND_ITEM_BY_ID =
                String.format("SELECT * FROM %s WHERE %s = ?;",
                        MAIN_TABLE_NAME, ITEMS_ID
                );
        private static final String FIND_ITEMS_BY_NAME =
                String.format("SELECT * FROM %s WHERE %s = ?;",
                        MAIN_TABLE_NAME, ITEMS_NAME
                );
        private static final String FIND_COMMENTS_BY_ITEMID =
                String.format("SELECT * FROM %s WHERE %s = ?;",
                        SECONDARY_TABLE_NAME, COMMENTS_ITEM_ID
                );
    }
}
