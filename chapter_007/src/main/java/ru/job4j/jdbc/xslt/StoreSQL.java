package ru.job4j.jdbc.xslt;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * StoreSQL - генерит N записей в базе,
 * передает List<Entry> в StoreXML
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class StoreSQL implements AutoCloseable {
    private static final String TABLE_NAME = "entries";
    private static final String FIELD_NAME = "field";
    private final Config config;
    private Connection connect;
    private DatabaseMetaData metaData;

    public StoreSQL(final Config config) {
        this.config = config;
        setConnection();
        createDBStructure();
    }

    public void generate(int size) throws SQLException {
        checkConnection();
        try {
            if (metaData.supportsBatchUpdates()) {
                insertUsingBatchUpdate(size);
            } else {
                throw new IllegalStateException("DB doesnt support BatchUpdates");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            connect.rollback();
        }
    }

    private void insertUsingBatchUpdate(int size) throws SQLException {
        connect.setAutoCommit(false);
        final Statement statement = this.connect.createStatement();
        for (int i = 0; i < size; i++) {
            statement.addBatch(
                    String.format("INSERT INTO %s VALUES (%s)",
                            TABLE_NAME, i)
            );
        }
        statement.executeBatch();
        connect.commit();
    }


    private void setConnection() {
        config.init();
        final String url = this.config.get("url"); //TODO check properties
        try {
            this.connect = DriverManager.getConnection(url);
            this.metaData = this.connect.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void checkConnection() throws SQLException {
        if (this.connect == null || this.connect.isClosed()) {
            setConnection();
        }
    }

    private void createDBStructure() {
        final String DROP_TABLE_IF_EXISTS = String
                .format("DROP TABLE IF EXISTS '%s'",
                        TABLE_NAME);
        final String CREATE_TABLE_IF_NOT_EXISTS = String
                .format("CREATE TABLE IF NOT EXISTS '%s' ( '%s' INTEGER NOT NULL )",
                        TABLE_NAME, FIELD_NAME);

        try (Statement statement = this.connect.createStatement()) {
            statement.executeUpdate(DROP_TABLE_IF_EXISTS);
            statement.executeUpdate(CREATE_TABLE_IF_NOT_EXISTS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Entry> load() {
        List<Entry> result = new ArrayList<>();
        final String SELECT_ALL_FROM_TABLE = String
                .format("SELECT * FROM '%s'",
                        TABLE_NAME);
        try (Statement statement = this.connect.createStatement()) {
            checkConnection();
            final ResultSet resultSet = statement.executeQuery(SELECT_ALL_FROM_TABLE);
            while (resultSet.next()) {
                result.add(new Entry(resultSet.getInt(FIELD_NAME)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result.isEmpty() ? Collections.EMPTY_LIST : result;
    }

    @Override
    public void close() {
        if (this.connect != null) {
            try {
                this.connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
