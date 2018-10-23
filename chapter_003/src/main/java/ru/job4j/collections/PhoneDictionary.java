package ru.job4j.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
     *
     * @param person Person to add
     */
    public void add(Person person) {
        this.persons.add(person);
    }

    /**
     * Method find - returns List of Persons, that contains search key in any field.
     *
     * @param key String search key.
     * @return List of Persons, in accordance to the search key.
     */
    public List<Person> find(String key) {

        if (key == null || key.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        Predicate<Person> personPredicate = person ->
                person.getName().toLowerCase().contains(key.toLowerCase())
                        || person.getSurname().toLowerCase().contains(key.toLowerCase())
                        || person.getPhone().toLowerCase().contains(key.toLowerCase())
                        || person.getAddress().toLowerCase().contains(key.toLowerCase());

        return this.persons.stream().filter(personPredicate).collect(Collectors.toList());
    }
}
