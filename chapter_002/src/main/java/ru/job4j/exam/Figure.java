package ru.job4j.exam;

/**
 * Figure
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public abstract class Figure {

    final Cell position;

    public abstract Cell[] way(Cell source, Cell destination) throws ImpossibleMoveException;

    public abstract Figure copy(Cell dest);

    /**
     * Figure instance constructor
     *
     * @param initCell initial Cell to place the Figure
     */
    public Figure(Cell initCell) {
        this.position = initCell;
    }
}
