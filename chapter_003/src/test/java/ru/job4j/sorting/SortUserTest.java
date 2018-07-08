package ru.job4j.sorting;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * TrackerTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class SortUserTest {

    /**
     * Test. User sorting.
     */
    @Test
    public void whenSortingUsersThenUsersSorted() {
        SortUser sortUser = new SortUser();

        User first = new User("Andrey", 20);
        User second = new User("Afanasy", 42);
        User third = new User("Ivan", 12);

        List<User> usersList = new ArrayList<>();
        usersList.add(first);
        usersList.add(second);
        usersList.add(third);

        Set<User> expected = new TreeSet<>();
        expected.add(second);
        expected.add(third);
        expected.add(first);

        Set<User> result = sortUser.sort(usersList);

        assertThat(result, is(expected));
    }

    /**
     * Test. Sorting by name length.
     */
    @Test
    public void whenSortingByNameThenSortedByName() {
        SortUser sortUser = new SortUser();

        User first = new User("Andrey", 20);
        User second = new User("Afanasy", 33);
        User third = new User("Ivan", 30);

        List<User> usersList = new ArrayList<>();
        usersList.add(first);
        usersList.add(second);
        usersList.add(third);

        List<User> result = sortUser.sortByNameLength(usersList);
        boolean rsl = result.get(0).getName().equals("Ivan")
                && result.get(1).getName().equals("Andrey")
                && result.get(2).getName().equals("Afanasy");

        assertThat(rsl, is(true));
    }

    /**
     * Test. Sorting by all fields.
     */
    @Test
    public void whenSortingByAllFieldsThenSortedByAllFields() {
        SortUser sortUser = new SortUser();

        User first = new User("Afanasii", 20);
        User second = new User("Afanasy", 33);
        User third = new User("Ivan", 30);
        User fourth = new User("Ivan", 17);

        List<User> usersList = new ArrayList<>();
        usersList.add(first);
        usersList.add(second);
        usersList.add(third);
        usersList.add(fourth);

        List<User> result = sortUser.sortByAllFields(usersList);
        boolean rsl = result.get(0).getName().equals("Afanasii")
                && result.get(1).getName().equals("Afanasy")
                && result.get(2).getName().equals("Ivan")
                && result.get(3).getName().equals("Ivan")
                && result.get(3).getAge() == 30;

        assertThat(rsl, is(true));
    }
}