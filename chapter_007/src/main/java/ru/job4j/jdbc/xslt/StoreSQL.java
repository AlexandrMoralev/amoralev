package ru.job4j.jdbc.xslt;

import java.io.File;
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
    private String tableName;
    private String fieldName;
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
        for (int i = 1; i <= size; i++) {
            statement.addBatch(
                    String.format("INSERT INTO %s VALUES (%s)",
                            tableName, i)
            );
        }
        statement.executeBatch();
        connect.commit();
    }

    private void setConnection() {
        config.init();
        final File dbfile = new File(".");
        final String dbname = this.config.get("dbname");
        final String url = this.config.get("url");
        try {
            this.connect = DriverManager.getConnection(
                    (url + dbfile.getAbsolutePath() + File.pathSeparator + dbname),
                    config.get("username"),
                    config.get("password")
            );
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
        tableName = this.config.get("tablename");
        fieldName = this.config.get("fieldname");
        final String DROP_TABLE_IF_EXISTS = String
                .format("DROP TABLE IF EXISTS '%s'",
                        tableName);
        final String CREATE_TABLE_IF_NOT_EXISTS = String
                .format("CREATE TABLE IF NOT EXISTS '%s' ( '%s' INTEGER NOT NULL )",
                        tableName, fieldName);

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
                        tableName);
        try (Statement statement = this.connect.createStatement()) {
            checkConnection();
            final ResultSet resultSet = statement.executeQuery(SELECT_ALL_FROM_TABLE);
            while (resultSet.next()) {
                result.add(new Entry(resultSet.getInt(fieldName)));
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

    public static void main(String[] args) throws SQLException {
        long start = System.nanoTime() / 1000000;
        System.out.println("start:" + start);

        final String sourceFilepath = "./entries.xml";
        final String destFilepath = "./entriesConverted.xml";
        final String schemeFilepath = "./schema.scm";

        StoreSQL storeSQL = new StoreSQL(new Config());
        storeSQL.generate(1000);
        Entries entries = new Entries(storeSQL.load());

        StoreXML storeXML = new StoreXML(new File(sourceFilepath));
        storeXML.save(entries.getEntries());

        ConvertXSQT convert = new ConvertXSQT();
        convert.convert(new File(sourceFilepath), new File(destFilepath), new File(schemeFilepath));

        long finish = System.nanoTime() / 1000000;
        System.out.println("finish:" + finish);
        System.out.println("time :" + (finish - start) + " ms");
    }

}
