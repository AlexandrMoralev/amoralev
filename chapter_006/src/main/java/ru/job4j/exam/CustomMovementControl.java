package ru.job4j.exam;

/**
 * CustomMovementControl - stub-class
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class CustomMovementControl implements MovementControl {
    @Override
    public Cell nextStep(final Cell currentCell) {
        return currentCell;
    }
}
