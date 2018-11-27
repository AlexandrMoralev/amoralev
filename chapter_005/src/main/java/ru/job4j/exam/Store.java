package ru.job4j.exam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Store
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Store {

    private Map<Integer, String> prev;

    /**
     * Method diff - returns statistics about collection changes
     *
     * @param previous source List of Users
     * @param current  changed List of Users
     * @return Info instance with the number of added, deleted and updated Users
     */
    public Info diff(final List<User> previous, final List<User> current) {
        validate(previous, current);
        int usersAdded = previous.isEmpty() && !current.isEmpty() ? current.size() : 0;
        int usersUpdated = 0;
        int usersDeleted = !previous.isEmpty() && current.isEmpty() ? previous.size() : 0;

        if (!previous.isEmpty() && !current.isEmpty()) {
            fillMap(previous);
            for (User user : current) {
                int id = user.getId();
                if (!prev.containsKey(id)) {
                    usersAdded++;
                } else {
                    if (!user.getName().equals(prev.get(id))) {
                        usersUpdated++;
                    }
                }
            }
            usersDeleted = previous.size() - current.size() + usersAdded;
        }
        return new Info(usersAdded, usersUpdated, usersDeleted);
    }

    private void fillMap(final List<User> previous) {
        this.prev = new HashMap<>();
        for (User user : previous) {
            prev.put(user.getId(), user.getName());
        }
    }

    private void validate(final List<User> first, final List<User> second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException();
        }
    }

    static class User {
        int id;
        String name;

        public User(final int id, final String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof User)) {
                return false;
            }
            User user = (User) o;
            return getId() == user.getId();
        }

        @Override
        public int hashCode() {
            return Objects.hash(getId());
        }
    }
}
