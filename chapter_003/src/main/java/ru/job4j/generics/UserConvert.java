package ru.job4j.generics;

import java.util.HashMap;
import java.util.List;

/**
 * UserConvert
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class UserConvert {
    /**
     * Method process - converts List of User into HashMap<Integer, User>.
     * When input List isEmpty, method returns empty HashMap.
     * @param list List<User> to convert
     * @return HashMap<Integer, User>, where K = User id, V = User
     */
    public HashMap<Integer, User> process(List<User> list) {
        HashMap<Integer, User> result = new HashMap<>();
        for (User user : list) {
            result.put(user.getId(), user);
        }
        return result;
    }
}
