package ru.job4j.exam;

import java.util.Objects;

/**
 * User
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class User {

    private String name;
    private String passport;

    /**
     * User instance constructor
     * @param name String user's name
     * @param passport String user's passport
     */
    public User(String name, String passport) {
        this.name = name;
        this.passport = passport;
    }

    /**
     * getName
     * @return String username
     */
    String getName() {
        return name;
    }

    /**
     * getPassport
     * @return String passport of the User
     */
    String getPassport() {
        return passport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(getName(), user.getName())
                && Objects.equals(getPassport(), user.getPassport());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPassport());
    }
}
