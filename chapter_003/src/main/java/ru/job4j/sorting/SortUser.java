package ru.job4j.sorting;

import java.util.*;

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
     * @param userList List<User> to sort
     * @return List<User> sorted by name length
     */
    public List<User> sortByNameLength(List<User> userList) {
        Comparator<User> nameLengthComparator = new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getName().length() - o2.getName().length();
            }
        };
        Collections.sort(userList, nameLengthComparator);
        return userList;
    }

    /**
     * Method sortByAllFields - sorting Users by all User fields
     * @param userList List<User> to sort
     * @return List<User> sorted by name first (lexicography), then by User's age
     */
    public List<User> sortByAllFields(List<User> userList) {
        Comparator<User> allFieldsComparator = new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                int result = o1.getName().compareTo(o2.getName());
                if (result == 0) {
                    result = Integer.compare(o1.getAge(), o2.getAge());
                }
                return result;
            }
        };
        Collections.sort(userList, allFieldsComparator);
        return userList;
    }

}
