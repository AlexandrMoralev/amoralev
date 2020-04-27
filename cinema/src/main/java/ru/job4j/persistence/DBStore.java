package ru.job4j.persistence;

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.Config;
import ru.job4j.model.Account;
import ru.job4j.model.Ticket;

import java.sql.*;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

/**
 * DBStore
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@ThreadSafe
public enum DBStore implements Store {
    INSTANCE;

    private static final Logger LOG = LogManager.getLogger(DBStore.class);
    private final Config config = Config.INSTANCE;
    private final BasicDataSource source = new BasicDataSource();
    private final Integer hallSize;

    DBStore() {
        config.getString("db.driver").ifPresent(source::setDriverClassName);
        config.getString("db.connection.url").ifPresent(source::setUrl);
        config.getString("db.user").ifPresent(source::setUsername);
        config.getString("db.pwd").ifPresent(source::setPassword);
        config.getInt("db.pool.idle.min").ifPresent(source::setMinIdle);
        config.getInt("db.pool.idle.max").ifPresent(source::setMaxIdle);
        config.getInt("db.pool.open-prepared-statements.max").ifPresent(source::setMaxOpenPreparedStatements);
        config.getInt("db.connection.lifetime-millis.max").ifPresent(source::setMaxConnLifetimeMillis);
        source.setValidationQuery("SELECT 1");
        this.hallSize = config.getInt("hall.size").orElse(null);
    }

    @Override
    public Collection<Ticket> getAllTickets() {
        try (Connection connection = source.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM hall;")
        ) {
            ResultSet rs = ps.executeQuery();
            Collection<Ticket> tickets = new ArrayDeque<>(hallSize);
            while (rs.next()) {
                tickets.add(extractTicket(rs));
            }
            return tickets;
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(String.format("DB error: %s", e.getMessage()));
        }
    }

    private Ticket extractTicket(ResultSet rs) throws SQLException {
        return Ticket.newBuilder()
                .setId(rs.getLong("ticket_id"))
                .setRow(rs.getInt("row"))
                .setSeat(rs.getInt("seat"))
                .setPrice(rs.getInt("price"))
                .setOrdered(ofNullable(rs.getObject("ordered")).isPresent())
                .build();
    }

    @Override
    public Optional<Long> createOrder(Collection<Integer> ticketIds, Account customer) {
        try (Connection connection = source.getConnection();
             PreparedStatement psa = connection.prepareStatement("INSERT INTO account (fio, phone) VALUES (?, ?) RETURNING account_id;");
             PreparedStatement pst = connection.prepareStatement("UPDATE hall SET ordered = ? WHERE ticket_id IN (?);")
        ) {
            connection.setAutoCommit(false);
            psa.setString(1, customer.getFio());
            psa.setString(2, customer.getPhone());
            ResultSet rs = psa.executeQuery();
            Optional<Long> accountId = Optional.empty();
            if (rs.next()) {
                accountId = Optional.of(rs.getLong(1));
            }
            Savepoint accountCreated = connection.setSavepoint();
            if (accountId.isEmpty()) {
                connection.rollback();
            }
            pst.setLong(1, accountId.get());
            pst.setString(2, ticketIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
            if (pst.executeUpdate() != ticketIds.size()) {
                connection.rollback(accountCreated);
            }
            connection.commit();
            connection.setAutoCommit(true);
            return accountId;
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(String.format("DB error: %s", e.getMessage()));
        }
    }
}
