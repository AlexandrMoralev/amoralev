package ru.job4j.collections;

import java.util.ArrayList;
import java.util.List;

/**
 * PhoneDictionary
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class PhoneDictionary {

    private List<Person> persons = new ArrayList<>();

    /**
     * Method add - adding new Person to the PhoneDictionary
     * @param person Person to add
     */
    public void add(Person person) {
        this.persons.add(person);
    }

    /**
     * Method find - returns List of Persons, that contains search key in any field.
     * @param key String search key.
     * @return List of Persons, in accordance to the search key.
     */
    public List<Person> find(String key) {

        List<Person> result = new ArrayList<>();

        if (!key.isEmpty()) {

            String uniKey = key.toLowerCase();

            for (Person person : this.persons) {

                if (null == person) {
                    break;
                }
                if (person.getName().toLowerCase().contains(uniKey)
                        || person.getSurname().toLowerCase().contains(uniKey)
                        || person.getPhone().toLowerCase().contains(uniKey)
                        || person.getAddress().toLowerCase().contains(uniKey)) {
                    result.add(person);
                }
            }
        }

        return result;
    }
}
