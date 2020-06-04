package ru.job4j.persistence;

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.Config;
import ru.job4j.model.Account;
import ru.job4j.model.Ticket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

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

    private Config config;
    private BasicDataSource source;
    private Integer hallSize;

    DBStore() {
        config = Config.INSTANCE;
        source = new BasicDataSource();
        applyConfig(config, source);
    }

    private void applyConfig(Config config, BasicDataSource source) {
        config.getString("cinema.db.driver").ifPresent(source::setDriverClassName);
        config.getString("cinema.db.connection.url").ifPresent(source::setUrl);
        config.getString("cinema.db.user").ifPresent(source::setUsername);
        config.getString("cinema.db.pwd").ifPresent(source::setPassword);
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
            Collection<Ticket> tickets = new ArrayList<>(hallSize);
            while (rs.next()) {
                tickets.add(extractTicket(rs));
            }
            return tickets;
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        return Collections.emptyList();
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
    public Optional<Long> createOrder(Collection<Long> ticketIds, Account customer) {
        try (Connection connection = source.getConnection();
             PreparedStatement selectAccId = connection.prepareStatement("SELECT account_id FROM accounts WHERE fio = ? AND phone = ?;");
             PreparedStatement checkTicketOrdered = connection.prepareStatement("SELECT ordered FROM hall WHERE ticket_id = ?;");
             PreparedStatement insertAcc = connection.prepareStatement("INSERT INTO accounts (fio, phone) VALUES (?, ?) RETURNING account_id;");
             PreparedStatement bindTicketToAcc = connection.prepareStatement("UPDATE hall SET ordered = ? WHERE ticket_id = ?;")
        ) {
            if (ticketIds.isEmpty()) {
                return Optional.empty();
            }
            connection.setAutoCommit(false);
            Optional<Long> accountId = getAccountIdFrom(selectAccId, customer)
                    .or(() -> getAccountIdFrom(insertAcc, customer));
            if (accountId.isEmpty() || ticketIds.stream().anyMatch(id -> isTicketReserved(checkTicketOrdered, id))) {
                connection.rollback();
                return Optional.empty();
            }
            for (Long id : ticketIds) {
                bindTicketToAcc.setLong(1, accountId.get());
                bindTicketToAcc.setLong(2, id);
                bindTicketToAcc.executeUpdate();
            }
            connection.commit();
            connection.setAutoCommit(true);
            return accountId;
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    private boolean isTicketReserved(PreparedStatement ps, Long ticketId) {
        try {
            ps.setLong(1, ticketId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.ofNullable(rs.getObject(1)).isPresent();
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        return false;
    }

    private Optional<Long> getAccountIdFrom(PreparedStatement ps, Account customer) {
        try {
            ps.setString(1, customer.getFio());
            ps.setString(2, customer.getPhone());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(rs.getLong(1));
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        return Optional.empty();
    }
}
