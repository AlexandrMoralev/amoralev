package ru.job4j.exam;

/**
 * AutoMovementControl
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class AutoMovementControl implements MovementControl {
    @Override
    public Cell nextStep(final Cell currentCell) {
        return randomNearbyCell(currentCell);
    }

    /**
     * Method randomNearbyCell - randomly generates the adjacent cell
     *
     * @param source notnull Cell for generation of nearby source Cell
     * @return randomly generated Cell.
     */
    private Cell randomNearbyCell(final Cell source) {
        int[] direction = this.getRandomDirection();
        int dX = direction[0];
        int dY = direction[1];
        return new Cell(source.getX() + dX, source.getY() + dY);
    }

    private int[] getRandomDirection() {
        int[] result = new int[2];
        int value = (int) (Math.random() * 100);
        if (value <= 25) {
            result[0] = 0;
            result[1] = 1;
        }
        if (value > 25 && value <= 50) {
            result[0] = -1;
            result[1] = 0;
        }
        if (value > 50 && value <= 75) {
            result[0] = 1;
            result[1] = 0;
        }
        if (value > 75) {
            result[0] = 0;
            result[1] = -1;
        }
        return result;
    }
}
