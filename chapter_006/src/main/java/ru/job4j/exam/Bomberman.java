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
     * @param position initial Cell
     */
    public Bomberman(final Cell position) {
        super(position);
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
