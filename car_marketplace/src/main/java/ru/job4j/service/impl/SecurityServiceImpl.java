package ru.job4j.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.entity.User;
import ru.job4j.persistence.impl.UsersDao;
import ru.job4j.service.SecurityService;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.time.LocalDateTime;
import java.util.Base64;

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

    private final UsersDao usersDao;

    public SecurityServiceImpl(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    @Override
    public boolean authenticateUser(String phone, String pass) {
        return usersDao.findByPhone(phone)
                .map(u -> getEncryptedPassword(pass, u.getSalt()).equals(u.getPassword()))
                .isPresent();
    }

    @Override
    public User signUp(String phone, String password) {
        String salt = getNewSalt();
        String encryptedPassword = getEncryptedPassword(password, salt);
        User user = User.create(
                phone,
                phone,
                salt,
                encryptedPassword,
                LocalDateTime.now()
        );
        this.usersDao.save(user);
        LOG.info("Signed up new user phone: {}", user.getPhone());
        return user;
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
        } catch (Exception e) {
            LOG.error("Can't generate token", e);
            throw new SecurityException("Security error");
        }
    }

    private String getEncryptedPassword(String pass, String salt) {
        int derivedKeyLength = 160; // for SHA1
        int iterations = 10001; // NIST specifies 10000

        byte[] saltBytes = Base64.getDecoder().decode(salt);
        KeySpec spec = new PBEKeySpec(pass.toCharArray(), saltBytes, iterations, derivedKeyLength);
        SecretKeyFactory factory = null;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new SecurityException("Security error");
        }

        try {
            byte[] encBytes = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(encBytes);
        } catch (Exception e) {
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
        } catch (Exception e) {
            LOG.error("Can't generate salt", e);
            throw new SecurityException("Security error");
        }
    }

}
