package ru.job4j.jdbc.xslt;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * StoreSQL - class for creating a new SQLite database;
 * generates, inserts and retrieves data from the database
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

    /**
     * Creates StoreSQL instance, establishes a DB Connection,
     * creates DB structure if it does not exists
     *
     * @param config Config instance
     */
    public StoreSQL(final Config config) {
        this.config = config;
        setConnection();
        createDBStructure();
    }

    /**
     * Generates items in the database
     *
     * @param items number of db-items to be generated
     * @throws SQLException
     */
    public void generate(int items) throws SQLException {
        checkConnection();
        try {
            if (metaData.supportsBatchUpdates()) {
                insertUsingBatchUpdate(items);
            } else {
                throw new IllegalStateException("DB doesnt support BatchUpdates");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            connect.rollback();
        }
    }

    /**
     * Inserts items into DB-table
     *
     * @param number number of db-items to be generated
     * @throws SQLException
     */
    private void insertUsingBatchUpdate(int number) throws SQLException {
        connect.setAutoCommit(false);
        final Statement statement = this.connect.createStatement();
        for (int i = 1; i <= number; i++) {
            statement.addBatch(
                    String.format("INSERT INTO %s VALUES (%s)",
                            tableName, i)
            );
        }
        statement.executeBatch();
        connect.commit();
    }

    /**
     * Initializes config, establishes a db-connection and gets DatabaseMetaData
     */
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

    /**
     * Creates a new database and db-tables if it doesn't exists.
     * Drop and creates a new db-table if it exists.
     */
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

    /**
     * Loads list of all items from the database
     *
     * @return List of db-items if the db contains any items,
     * empty list otherwise
     */
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

    public static void main(String[] args) throws SQLException, TransformerException, IOException, SAXException, ParserConfigurationException {
        int numberOfValues = 1_000_000;

        final String sourceFilepath = "./entries.xml";
        final String destFilepath = "./entriesConverted.xml";
        final String schemeFilepath = "./scheme.scm";

        long start = System.nanoTime();
        System.out.println("start executing...");

        StoreSQL storeSQL = new StoreSQL(new Config());
        storeSQL.generate(numberOfValues);
        Entries entries = new Entries(storeSQL.load());

        StoreXML storeXML = new StoreXML(new File(sourceFilepath));
        storeXML.save(entries.getEntries());

        ConvertXSQT convert = new ConvertXSQT();
        File destFile = new File(destFilepath);
        convert.convert(new File(sourceFilepath),
                destFile,
                new File(schemeFilepath)
        );
        SAXEntriesParser parser = new SAXEntriesParser(destFile);

        long finish = System.nanoTime();
        System.out.println("finished successfully!");
        System.out.println(">>> arithmetic sum of all values: " + parser.parseSum());

        System.out.println(String.format("parsing time: %s ms @%s items", (finish - start) / 1000000, numberOfValues));
    }

}
