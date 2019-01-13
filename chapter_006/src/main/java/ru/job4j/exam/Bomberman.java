package ru.job4j.exam;

/**
 * Bomberman
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Bomberman extends GameObject {

    /**
     * Bomberman default instance constructor
     *
     * @param position Ce
     */
    public Bomberman(final Cell position) {
        super(position);
    }

    /**
     * Constructs Bomberman instance by the initial cell coordinates
     *
     * @param x int x
     * @param y int y
     */
    public Bomberman(final int x, final int y) {
        this(new Cell(x, y));
    }

    /**
     * Updates position on the Board
     *
     * @param cell notnull Cell - new position
     */
    @Override
    public void setPosition(final Cell cell) {
        super.setPosition(cell);
    }
}
