package ru.job4j.maps;

import java.util.HashMap;
import java.util.Map;

/**
 * UserMap
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class UserMap {

    private final Map<User, Object> map;

    public UserMap() {
        this.map = new HashMap<>();
    }

    public Map<User, Object> getMap() {
        return this.map;
    }

    public boolean add(User user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        Object result = this.map.put(user, new Object());
        return result != null;
    }

    @Override
    public String toString() {
        return "UserMap{"
                + "map=" + map
                + '}' + System.lineSeparator();
    }
}
