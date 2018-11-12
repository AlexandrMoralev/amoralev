package ru.job4j.maps;

import java.util.Calendar;
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
    private int children;
    private Calendar birthday;

    /**
     * User instance constructor
     *
     * @param name     String
     * @param children int
     * @param birthday Calendar birth date
     */
    public User(String name, int children, Calendar birthday) {
        this.name = name;
        this.children = children;
        this.birthday = birthday;
    }

    public String getName() {
        return this.name;
    }

    public int getChildren() {
        return this.children;
    }

    public Calendar getBirthday() {
        return this.birthday;
    }

    @Override
    public String toString() {
        return "User{"
                + "name='" + this.name + '\''
                + ", children=" + this.children
                + ", birthday=" + this.birthday
                + '}';
    }

    @Override
    public int hashCode() {
        return this.name.hashCode() * 31
                + this.children * 19
                + this.birthday.hashCode() * 11;
    }
}
