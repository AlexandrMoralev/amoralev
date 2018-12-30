package ru.job4j.nonblocking;

import java.util.Objects;

/**
 * Base
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Base {
    private final int id;
    private int version;

    /**
     * Constructs Base instance
     *
     * @param id int identifier of constructed Base
     */
    public Base(final int id) {
        this.id = id;
        this.version = 0;
    }

    public int getId() {
        return this.id;
    }

    public int getVersion() {
        return this.version;
    }

    /**
     * Method updateVersion - increments version of the Base
     */
    public void updateVersion() {
        this.version++;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Base)) {
            return false;
        }
        Base base = (Base) o;
        return getId() == base.getId()
                && getVersion() == base.getVersion();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getVersion());
    }

    @Override
    public String toString() {
        return "Base{"
                + "id=" + id
                + ", version=" + version
                + '}';
    }
}
