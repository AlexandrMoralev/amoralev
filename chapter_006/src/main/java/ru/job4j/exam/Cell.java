package ru.job4j.exam;

import java.util.Objects;

/**
 * Cell
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Cell {
    private final int x;
    private final int y;

    /**
     * Constructs Cell instance
     *
     * @param x int x-coordinate
     * @param y int y-coordinate
     */
    public Cell(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cell)) {
            return false;
        }
        Cell cell = (Cell) o;
        return getX() == cell.getX()
                && getY() == cell.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }

    @Override
    public String toString() {
        return "Cell{"
                + "x=" + x
                + ", y=" + y
                + '}';
    }
}
