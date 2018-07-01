package ru.job4j.sorting;

/**
 * User
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class User implements Comparable<User> {

    private String name;
    private int age;

    /**
     * User instance constructor
     * @param name String name of the User
     * @param age int User's age
     */
    public User(String name, int age) {
        this.name = name;
        if (age >= 0) {
            this.age = age;
        }
    }

    /**
     * getName
     * @return String name
     */
    String getName() {
        return name;
    }

    /**
     * getAge
     * @return int age
     */
    int getAge() {
        return age;
    }

    /**
     * Method compareTo - compares two Users by age
     * @param o User to compare
     * @return int 0 if Users are equal,
     * int <0 when the first User is less than the second,
     * and int >0 when the first User is greater than the second
     */
    @Override
    public int compareTo(User o) {
        return this.age - o.getAge();
    }
}
