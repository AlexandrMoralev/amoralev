package ru.job4j.sorting;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
}
