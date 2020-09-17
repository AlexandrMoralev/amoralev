package ru.job4j.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.domain.Role;
import ru.job4j.domain.User;
import ru.job4j.exception.SecurityException;
import ru.job4j.persistence.Dao;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

/**
 * SecurityServiceImpl
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class SecurityServiceImpl implements SecurityService {

    private static final Logger LOG = LogManager.getLogger(SecurityServiceImpl.class);

    private static final String SYMBOLS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private final Dao<User> usersDao;

    public SecurityServiceImpl(Dao<User> usersDao) {
        this.usersDao = usersDao;
        LOG.info("SecurityServiceImpl created");
    }

    public SecurityService init() {
        User admin = new User();
        admin.setName("admin");
        admin.setPassword("admin");
        admin.setRole(Role.ADMIN);
        admin.setTasks(Collections.emptyList());
        this.signUp(admin);
        LOG.info("SecurityServiceImpl: admin added");
        return this;
    }

    @Override
    public boolean authenticateUser(String login, String pass) {
        List<User> users = usersDao.findByCriteria("name", login);
        if (users.size() > 1) {
            LOG.error("Found {} users by login {} ", users.size(), login);
            throw new SecurityException("service error");
        }
        return users.stream()
                .anyMatch(u -> {
                    String calculatedHash = getEncryptedPassword(pass, u.getSalt());
                    return calculatedHash.equals(u.getPassword());
                });
    }

    @Override
    public User signUp(User newUser) {
        String salt = getNewSalt();
        String encryptedPassword = getEncryptedPassword(newUser.getPassword(), salt);
        User user = new User();
        user.setName(newUser.getName());
        user.setPassword(encryptedPassword);
        user.setSalt(salt);
        user.setRole(newUser.getRole());
        user.setTasks(Collections.emptyList());
        User registeredUser = this.usersDao.save(user);
        LOG.info("Signed up new user login: {}", newUser.getName());
        return registeredUser;
    }

    @Override
    public String generateToken() {
        int strLength = 36;
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            StringBuilder sb = new StringBuilder(strLength);
            for (int i = 0; i < strLength; i++) {
                sb.append(SYMBOLS.charAt(random.nextInt(SYMBOLS.length())));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Can't generate token", e);
            throw new SecurityException("Security error");
        }
    }

    private String getEncryptedPassword(String pass, String salt) {
        String algorithm = "PBKDF2WithHmacSHA1";
        int derivedKeyLength = 160; // for SHA1
        int iterations = 10001; // NIST specifies 10000 20000

        byte[] saltBytes = Base64.getDecoder().decode(salt);
        KeySpec spec = new PBEKeySpec(pass.toCharArray(), saltBytes, iterations, derivedKeyLength);
        SecretKeyFactory factory = null;
        try {
            factory = SecretKeyFactory.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            LOG.error(e.getMessage(), e);
            throw new SecurityException("Security error");
        }

        try {
            byte[] encBytes = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(encBytes);
        } catch (InvalidKeySpecException e) {
            LOG.error("Can't generate secret", e);
            throw new SecurityException("Security error");
        } finally {
            LOG.info("EncryptedPassword generated");
        }
    }

    private String getNewSalt() {
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[8]; // NIST specifies 4
            random.nextBytes(salt);
            return Base64.getEncoder().encodeToString(salt);
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Can't generate salt", e);
            throw new SecurityException("Security error");
        }
    }

}
