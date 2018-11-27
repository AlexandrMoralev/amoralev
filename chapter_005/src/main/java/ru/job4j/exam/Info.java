package ru.job4j.exam;

import java.util.Objects;

/**
 * Info
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Info {
    private Integer usersCreated;
    private Integer usersUpdated;
    private Integer usersDeleted;

    /**
     * Constructs Info instance with specified parameters
     *
     * @param usersCreated Integer number of new Users
     * @param usersUpdated Integer number of updated Users
     * @param usersDeleted Integer number of deleted Users
     */
    public Info(Integer usersCreated, Integer usersUpdated, Integer usersDeleted) {
        this.usersCreated = usersCreated;
        this.usersUpdated = usersUpdated;
        this.usersDeleted = usersDeleted;
    }

    public Integer getUsersCreated() {
        return usersCreated;
    }

    public Integer getUsersUpdated() {
        return usersUpdated;
    }

    public Integer getUsersDeleted() {
        return usersDeleted;
    }

    @Override
    public String toString() {
        return "Info{"
                + " usersCreated=" + usersCreated
                + ", usersUpdated=" + usersUpdated
                + ", usersDeleted=" + usersDeleted
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Info)) {
            return false;
        }
        Info info = (Info) o;
        return Objects.equals(getUsersCreated(), info.getUsersCreated())
                && Objects.equals(getUsersUpdated(), info.getUsersUpdated())
                && Objects.equals(getUsersDeleted(), info.getUsersDeleted()
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsersCreated(),
                getUsersUpdated(),
                getUsersDeleted()
        );
    }
}
