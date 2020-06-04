package ru.job4j;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public enum DbUtil {
    INSTANCE;

    private static final Logger LOG = LogManager.getLogger(DbUtil.class);

    private Config config = Config.INSTANCE;

    DbUtil() {
    }

    public void performMigrations() {
        Liquibase liquibase = null;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(config.getString("cinema.db.connection.url").orElse("db.connection.url"),
                    config.getString("cinema.db.user").orElse("db.user"),
                    config.getString("cinema.db.pwd").orElse("db.pwd"));

            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            String changelog = config.getString("db.changeLogFile").orElse("db.changeLogFile");
            liquibase = new Liquibase(changelog, new ClassLoaderResourceAccessor(), database);
            liquibase.update("test");
        } catch (SQLException | LiquibaseException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            if (connection != null) {
                try {
                    connection.rollback();
                    connection.close();
                } catch (SQLException e) {
                    //nothing to do
                }
            }
        }
    }

    public void cleanUp() {
        try (Connection connection = DriverManager.getConnection(config.getString("cinema.db.connection.url").orElse("db.connection.url"),
                config.getString("cinema.db.user").orElse("db.user"),
                config.getString("cinema.db.pwd").orElse("db.pwd"));
             PreparedStatement preparedStatement = connection.prepareStatement("TRUNCATE TABLE hall; TRUNCATE TABLE accounts; TRUNCATE TABLE databasechangelog; TRUNCATE TABLE databasechangeloglock;")
        ) {
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

}

