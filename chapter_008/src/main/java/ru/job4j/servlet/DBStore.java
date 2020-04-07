package ru.job4j.servlet;

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.controllers.Config;
import ru.job4j.crudservlet.Address;
import ru.job4j.crudservlet.Store;
import ru.job4j.crudservlet.User;
import ru.job4j.filtersecurity.Role;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Optional;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

/**
 * DBStore - persistence layout
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@ThreadSafe
public enum DBStore implements Store<User> {
    INSTANCE;

    private static final Logger LOG = LogManager.getLogger(DBStore.class);

    private static final String INSERT_INTO_USERS = "INSERT INTO users(name,login,created,pwd,role_desc,address_id) VALUES(?,?,?,?,?,?) RETURNING id;";
    private static final String INSERT_INTO_ADDRESS = "INSERT INTO address(country,city) VALUES(?,?) RETURNING id;";
    private static final String UPDATE_USER = "UPDATE users SET name = ?, login = ?, created = ?, pwd = ?, role_desc = ?, address_id = ? WHERE id = ? ;";
    private static final String DELETE_USER_BY_ID = "DELETE FROM users WHERE id = ? ;";
    private static final String SELECT_ALL_USERS = "SELECT u.name, u.login, u.created, u.role_desc, a.country, a.city FROM users as u JOIN address as a ON u.address_id = a.id ;";
    private static final String SELECT_USER_BY_ID = "SELECT u.name, u.login, u.created, u.role_desc, a.country, a.city FROM users as u JOIN address as a ON u.address_id = a.id WHERE u.id = ? ;";
    private static final String SELECT_USER_BY_LOGIN = "SELECT u.name, u.login, u.created, u.role_desc, a.country, a.city FROM users as u JOIN address as a ON u.address_id = a.id WHERE u.login = ? ;";
    private static final String SELECT_USER_BY_COUNTRY = "SELECT u.name, u.login, u.created, u.role_desc, a.country, a.city FROM users as u JOIN address as a ON u.address_id = a.id WHERE a.country = ? ;";
    private static final String SELECT_USER_BY_CITY = "SELECT u.name, u.login, u.created, u.role_desc, a.country, a.city FROM users as u JOIN address as a ON u.address_id = a.id WHERE a.city = ? ;";

    private static final String CHECK_CREDENTIAL = "SELECT id FROM users WHERE login = ? AND pwd = ?;";

    private static final String SELECT_ADDRESS_COUNTRIES = "SELECT DISTINCT country FROM address ORDER BY country ASC;";
    private static final String SELECT_ADDRESS_BY_COUNTRY = "SELECT * FROM address WHERE country = ? ORDER BY city ASC;";
    private static final String SELECT_ADDRESS_BY_ID = "SELECT * FROM address WHERE id = ?;";
    private static final String FIND_ADDRESS_ID = "SELECT id FROM address WHERE country = ? AND city = ?;";

    private BasicDataSource source;
    private Config config;

    DBStore() {
        config = new Config();
        source = new BasicDataSource();
        initSource();
        initDB();
    }

    private void initSource() {
        source.setDriverClassName(config.getString("db.driver"));
        source.setUrl(config.getString("db.connection.url") + config.getString("db.name"));
        source.setUsername(config.getString("db.user"));
        source.setPassword(config.getString("db.pwd"));
        source.setMinIdle(config.getInt("db.idle.min"));
        source.setTimeBetweenEvictionRunsMillis(Long.valueOf(config.getInt("db.time.between.evictions")));
        source.setMaxIdle(config.getInt("db.idle.max"));
        source.setMaxOpenPreparedStatements(config.getInt("db.max.open.prepared.statements"));
    }

    private void initDB() {
        try (Connection connection = DriverManager.getConnection(config.getString("db.connection.url"), config.getString("db.user"), config.getString("db.pwd"));
             Statement st = connection.createStatement()
        ) {
            String checkDatabaseExists = String.format("SELECT EXISTS(SELECT * FROM pg_database WHERE datname = '%s');", config.getString("db.name"));
            ResultSet rs = st.executeQuery(checkDatabaseExists);
            if (rs.next()) {
                if (!rs.getBoolean(1)) {
                    String createDatabase = String.format("CREATE DATABASE %s;", config.getString("db.name"));
                    st.execute(createDatabase);
                }
            }
        } catch (SQLException e) {
            LOG.error("DB init error", e);
        }
        try (Connection connection = source.getConnection();
             Statement st = connection.createStatement()
        ) {
            st.execute("CREATE TABLE IF NOT EXISTS address("
                    + "id SERIAL PRIMARY KEY NOT NULL,"
                    + " country VARCHAR(256) NOT NULL,"
                    + " city VARCHAR(256) NOT NULL,"
                    + "CONSTRAINT unq_country_city UNIQUE (country, city)"
                    + ");");
            st.execute("CREATE TABLE IF NOT EXISTS users("
                    + "id SERIAL PRIMARY KEY NOT NULL,"
                    + " name VARCHAR(128) NOT NULL,"
                    + " login VARCHAR(256) NOT NULL,"
                    + " created VARCHAR(64),"
                    + " pwd VARCHAR(128) NOT NULL,"
                    + " role_desc VARCHAR(64) NOT NULL,"
                    + " address_id INT REFERENCES address(id),"
                    + "CONSTRAINT unq_login UNIQUE (login)"
                    + ");");
            st.execute("TRUNCATE TABLE users;");
            st.execute("TRUNCATE TABLE address;");
            addInitData(connection);
        } catch (SQLException e) {
            LOG.error("DB init error", e);
        }
    }

    private void addInitData(Connection connection) throws SQLException {
        try (PreparedStatement psa = connection.prepareStatement(INSERT_INTO_ADDRESS);
             PreparedStatement psu = connection.prepareStatement(INSERT_INTO_USERS)
        ) {
            Integer addressId = addAddress("Russia", "Spb", psa);
            addAddress("Russia", "Moscow", psa);
            addAddress("Russia", "NN", psa);
            addAddress("USA", "New York", psa);
            addAddress("USA", "Washington", psa);
            addAddress("England", "London", psa);
            addAddress("England", "Cambridge", psa);
            setUserQueryParameters(
                    User.newBuilder()
                            .setId(0)
                            .setName("root")
                            .setLogin("root@root.ru")
                            .setCreated(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()))
                            .setPassword("root")
                            .setRole(Role.ROOT)
                            .setAddress(Address.newBuilder().setId(addressId).build())
                            .build(),
                    psu);
            psu.execute();
        }
    }

    @Override
    public Optional<Integer> add(User user) {
        try (Connection connection = source.getConnection();
             PreparedStatement psu = connection.prepareStatement(INSERT_INTO_USERS);
             PreparedStatement psai = connection.prepareStatement(INSERT_INTO_ADDRESS);
             PreparedStatement psas = connection.prepareStatement(FIND_ADDRESS_ID)
        ) {
            connection.setAutoCommit(false);
            Integer addressId = findOrInsertAddress(user, psai, psas);
            setUserQueryParameters(
                    User.newBuilder().of(user)
                            .setAddress(Address.newBuilder().setId(addressId).build())
                            .build(),
                    psu);
            Optional<Integer> result = Optional.empty();
            if (psu.executeUpdate() > 0) {
                ResultSet rsu = psu.getGeneratedKeys();
                result = of(rsu.getInt(1));
            }
            connection.commit();
            connection.setAutoCommit(true);
            return result;
        } catch (SQLException e) {
            LOG.error("DB error", e);
            throw new RuntimeException("DB insert error");
        }
    }

    @Override
    public boolean update(User user) {
        try (Connection connection = source.getConnection();
             PreparedStatement psu = connection.prepareStatement(UPDATE_USER);
             PreparedStatement psai = connection.prepareStatement(INSERT_INTO_ADDRESS);
             PreparedStatement psas = connection.prepareStatement(FIND_ADDRESS_ID)
        ) {
            connection.setAutoCommit(false);
            Integer addressId = findOrInsertAddress(user, psai, psas);
            setUserQueryParameters(
                    User.newBuilder().of(user)
                            .setAddress(Address.newBuilder().setId(addressId).build())
                            .build(),
                    psu);
            psu.setInt(7, user.getId());
            int result = psu.executeUpdate();

            if (result == 1) {
                connection.commit();
                connection.setAutoCommit(true);
                return true;
            } else {
                throw new SQLException(result + " users updated");
            }
        } catch (SQLException e) {
            LOG.error("DB error", e);
            throw new RuntimeException("DB update error");
        }
    }

    @Override
    public void delete(int userId) {
        try (Connection connection = source.getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_USER_BY_ID)
        ) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error("DB error", e);
            throw new RuntimeException("DB error deleting user");
        }
    }

    @Override
    public Collection<User> findAll() {
        return getUsers(SELECT_ALL_USERS, "DB findAll error");
    }

    @Override
    public Optional<User> findById(int id) {
        try (Connection connection = source.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_USER_BY_ID)
        ) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return of(extractUser(rs));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            LOG.error("DB error", e);
            throw new RuntimeException("DB findById error");
        }
    }

    @Override
    public Optional<User> findByLogin(String login) {
        try (Connection connection = source.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_USER_BY_LOGIN)
        ) {
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return of(extractUser(rs));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            LOG.error("DB error", e);
            throw new RuntimeException("DB findByLogin error");
        }
    }

    @Override
    public Collection<User> findByCountry(String country) {
        return getUsers(SELECT_USER_BY_COUNTRY, "DB findByCountry error");
    }

    @Override
    public Collection<User> findByCity(String city) {
        return getUsers(SELECT_USER_BY_CITY, "DB findByCity error");
    }

    @Override
    public Optional<Address> getAddress(int id) {
        try (Connection connection = source.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_ADDRESS_BY_ID)
        ) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return of(extractAddress(rs));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            LOG.error("DB error", e);
            throw new RuntimeException("DB findByLogin error");
        }
    }

    @Override
    public Collection<String> getAllCountries() {
        try (Connection connection = source.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_ADDRESS_COUNTRIES)
        ) {
            ResultSet rs = ps.executeQuery();
            Collection<String> countries = new ArrayDeque<>();
            while (rs.next()) {
                countries.add(rs.getString(1));
            }
            return countries;
        } catch (SQLException e) {
            LOG.error("DB error", e);
            throw new RuntimeException("DB getAllCountries error");
        }
    }

    @Override
    public Collection<Address> getAddressesInCountry(String country) {
        try (Connection connection = source.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_ADDRESS_BY_COUNTRY)
        ) {
            ps.setString(1, country);
            ResultSet rs = ps.executeQuery();
            Collection<Address> addresses = new ArrayDeque<>();
            while (rs.next()) {
                addresses.add(extractAddress(rs));
            }
            return addresses;
        } catch (SQLException e) {
            LOG.error("DB error", e);
            throw new RuntimeException("DB getAddresses error");
        }
    }

    @Override
    public boolean isCredential(String login, String password) {
        try (Connection connection = source.getConnection();
             PreparedStatement ps = connection.prepareStatement(CHECK_CREDENTIAL)
        ) {
            ps.setString(1, login);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            Collection<User> users = new ArrayDeque<>();
            while (rs.next()) {
                users.add(extractUser(rs));
            }
            if (users.size() > 1) {
                throw new SQLException(users.size() + " has the same credentials");
            }
            return users.size() == 1;
        } catch (SQLException e) {
            LOG.error("DB error", e);
            throw new RuntimeException("DB isCredential error");
        }
    }

    private Collection<User> getUsers(String query, String errMsg) {
        try (Connection connection = source.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)
        ) {
            ResultSet rs = ps.executeQuery();
            Collection<User> allUsers = new ArrayDeque<>();
            while (rs.next()) {
                allUsers.add(extractUser(rs));
            }
            return allUsers;
        } catch (SQLException e) {
            LOG.error("DB error", e);
            throw new RuntimeException(errMsg);
        }
    }

    private void setUserQueryParameters(User user, PreparedStatement ps) throws SQLException {
        ps.setString(1, user.getName());
        ps.setString(2, user.getLogin());
        ps.setString(3, user.getCreated());
        ps.setString(4, user.getPassword());
        ps.setString(5, user.getRole().getDescription());
        ps.setInt(6, user.getAddress().getId());
    }

    private User extractUser(ResultSet resultSet) throws SQLException {
        User.Builder user = User.newBuilder();
        of(resultSet.getInt(1)).ifPresent(user::setId);
        of(resultSet.getString(2)).ifPresent(user::setName);
        of(resultSet.getString(3)).ifPresent(user::setLogin);
        ofNullable(resultSet.getString(4)).ifPresent(user::setCreated);
        of(resultSet.getString(5)).ifPresent(user::setPassword);
        of(resultSet.getString(6)).map(Role::valueOf).ifPresent(user::setRole);
        user.setAddress(Address.newBuilder()
                .setId(resultSet.getInt(7))
                .setCountry(resultSet.getString(8))
                .setCity(resultSet.getString(9))
                .build());
        return user.build();
    }
    private Integer addAddress(String country, String city, PreparedStatement ps) {
        try {
            ps.setString(1, country);
            ps.setString(2, city);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new RuntimeException("DB insert address error");
            }
        } catch (SQLException e) {
            LOG.error("DB insert address error", e);
            throw new RuntimeException("DB insert address error");
        }
    }

    private Optional<Integer> findAddressId(String country, String city, PreparedStatement ps) throws SQLException {
        ps.setString(1, country);
        ps.setString(2, city);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return of(rs.getInt(1));
        } else {
            return Optional.empty();
        }
    }

    private Integer findOrInsertAddress(User user, PreparedStatement insert, PreparedStatement select) throws SQLException {
        String country = user.getAddress().getCountry();
        String city = user.getAddress().getCity();
        return findAddressId(country, city, select)
                .orElseGet(() -> addAddress(country, city, insert));
    }

    private Address extractAddress(ResultSet rs) throws SQLException {
        return Address.newBuilder()
                .setId(rs.getInt(1))
                .setCountry(rs.getString(2))
                .setCity(rs.getString(3))
                .build();
    }

}
