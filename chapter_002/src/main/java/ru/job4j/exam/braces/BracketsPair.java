package ru.job4j.exam.braces;

import java.util.Objects;

/**
 * BracketsPair
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class BracketsPair {

    private int opened;
    private int closed;

    /**
     * Constructs BracketsPair with int positions of opening and closing bracket in the parsed string.
     *
     * @param opened int opening bracket position
     * @param closed int closing bracket position
     */
    public BracketsPair(int opened, int closed) {
        this.opened = opened;
        this.closed = closed;
    }

    public int getOpened() {
        return opened;
    }

    public int getClosed() {
        return closed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BracketsPair)) {
            return false;
        }
        BracketsPair that = (BracketsPair) o;
        return getOpened() == that.getOpened()
                && getClosed() == that.getClosed();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOpened(), getClosed());
    }
}
