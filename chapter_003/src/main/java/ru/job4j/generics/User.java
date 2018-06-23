package ru.job4j.generics;

/**
 * User
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class User {

    private int id;
    private String name;
    private String city;

    /**
     * User instance constructor
     * @param id int User id
     * @param name String name of the User
     * @param city String city of the User
     */
    public User(int id, String name, String city) {
        this.id = id;
        this.name = name;
        this.city = city;
    }

    /**
     * getId
     * @return int User's id
     */
    public int getId() {
        return id;
    }

    /**
     * getName
     * @return String Users's name
     */
    public String getName() {
        return name;
    }

    /**
     * getCity
     * @return String User's city
     */
    public String getCity() {
        return city;
    }
}
