package ru.job4j.collections;

/**
 * Person
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Person {

    private String name;
    private String surname;
    private String phone;
    private String address;

    /**
     * Person instance constructor
     * @param name String name of the Person
     * @param surname String surname of the Person
     * @param phone String phone number of the Person
     * @param address String address of the Person
     */
    public Person(String name, String surname, String phone, String address) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.address = address;
    }

    /**
     * getName
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * getSurname
     * @return String surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * getPhone
     * @return String phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * getAddress
     * @return String address
     */
    public String getAddress() {
        return address;
    }
}
