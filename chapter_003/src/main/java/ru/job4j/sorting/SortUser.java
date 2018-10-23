package ru.job4j.sorting;

import java.util.*;
import java.util.stream.Collectors;

/**
 * SortUser
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class SortUser {

    public Set<User> sort(List<User> userList) {
        return new TreeSet<>(userList);
    }

    /**
     * Method sortByNameLength - sorting Users by name length
     *
     * @param userList List<User> to sort
     * @return List<User> sorted by name length
     */
    public List<User> sortByNameLength(List<User> userList) {
        Comparator<User> nameLengthComparator = Comparator.comparingInt(o -> o.getName().length());
        return userList.stream().sorted(nameLengthComparator).collect(Collectors.toList());
    }

    /**
     * Method sortByAllFields - sorting Users by all User fields
     *
     * @param userList List<User> to sort
     * @return List<User> sorted by name first (lexicography), then by User's age
     */
    public List<User> sortByAllFields(List<User> userList) {
        Comparator<User> allFieldsComparator =  Comparator.comparing(User::getName).thenComparingInt(User::getAge);
        return userList.stream().sorted(allFieldsComparator).collect(Collectors.toList());
    }

}
