package ru.job4j.generics;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * UserConvertTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class UserConvertTest {

    /**
     * Test. When input List is empty, returns empty HashMap<Integer, User>.
     */
    @Test
    public void whenInputListIsEmptyThenEmptyHashMap() {
        UserConvert converter = new UserConvert();
        HashMap<Integer, User> expected = new HashMap<>();
        List<User> users = Collections.EMPTY_LIST;
        HashMap<Integer, User> result = converter.process(users);
        assertThat(result, is(expected));
    }

    /**
     * Test. When List<User>.size() == 3, then output HashMap has the same 3 Users.
     */
    @Test
    public void whenListW3UsersThenHashMapW3UsersById() {
        UserConvert converter = new UserConvert();
        User firstUser = new User(
                1,
                "Andrey",
                "Spb"
        );
        User secondUser = new User(
                3,
                "Ivan",
                "Nsk"
        );
        User thirdUser = new User(
                -4,
                "Gennady",
                "Omsk"
        );
        List<User> users = new ArrayList<>();
        users.add(firstUser);
        users.add(secondUser);
        users.add(thirdUser);
        HashMap<Integer, User> result = converter.process(users);
        HashMap<Integer, User> expected = new HashMap<>();
        expected.put(firstUser.getId(), firstUser);
        expected.put(secondUser.getId(), secondUser);
        expected.put(thirdUser.getId(), thirdUser);
        assertThat(result, is(expected));
    }
}
