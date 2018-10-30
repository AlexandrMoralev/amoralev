package ru.job4j.exam;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * Board
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Board {

    final static int FIGURES = 32;
    final static int DIMENSION = 8;
    private Figure[] figures = new Figure[FIGURES];
    private int position = 0;

    /**
     * Method add - adds Figure on the Board
     * if the Board is full of Figures, prints error message to console
     *
     * @param figure Figure to add
     */
    void add(Figure figure) {
        if (this.position != FIGURES) {
            this.figures[position++] = figure;
        } else {
            System.out.println("Board is already filled with pawns and pieces. You cannot add more figures to the Board.");
        }
    }

    /**
     * Method move - moves the Figure on Board
     * checks is there Figure at source Cell, is the Figure able to move to destination Cell, is any other Figures on the way
     *
     * @param source      initial Cell to move
     * @param destination destination Cell
     * @return true if thr Figure moved successful, false if it's not
     * @throws ImpossibleMoveException when it's impossible to move the Figure that way
     * @throws OccupiedWayException    when there is other Figure on the way
     * @throws FigureNotFoundException when there is no Figure in source Cell
     */
    boolean move(Cell source, Cell destination) throws ImpossibleMoveException, OccupiedWayException, FigureNotFoundException {

        int figurePosition;
        Cell[] wayToMove;

        figurePosition = figureAtCell(source);

        if (figurePosition >= FIGURES) {
            throw new FigureNotFoundException(String.format("There is no figure at %s - %s ", source.getRank(), source.getFile()));
        }

        wayToMove = figures[figurePosition].way(source, destination);

        if (null == wayToMove || !isWayClean(wayToMove)) {
            throw new ImpossibleMoveException(String.format("The %s can't move to destination cell", figures[figurePosition].getClass().getSimpleName()));
        }

        this.figures[figurePosition] = this.figures[figurePosition].copy(destination);

        return true;
    }

    /**
     * Method figureAtCell - checks is there a Figure at the Cell
     *
     * @param aCell Cell to check
     * @return int position of the Figure at the Board, if it's exists, else returns int == FIGURES (out of bounds)
     */
    private int figureAtCell(Cell aCell) {

        int result = FIGURES;
        int count = 0;

        for (Figure figure : figures) {
            if (null == figure) {
                break;
            }
            if (figure.position.equals(aCell)) {
                result = count;
                break;
            } else {
                count++;
            }
        }
        return result;
    }

    /**
     * Method getFigureAtCell
     *
     * @param aCell target Cell to check on Figure
     * @return Figure at target Cell, or null if there is no Figure at the target Cell
     */
    public Figure getFigureAtCell(Cell aCell) {

        Figure result = null;
        int positionAtBoard = this.figureAtCell(aCell);

        if (positionAtBoard < Board.FIGURES) {
            result = this.figures[positionAtBoard];
        }
        return result;
    }

    /**
     * Method isWayClean
     *
     * @param wayToMove Cell[] way to check
     * @return true when way is clean, and false when it's not
     * @throws OccupiedWayException when there is any other Figure on the way (except Knight)
     */
    private boolean isWayClean(Cell[] wayToMove) throws OccupiedWayException {

        boolean result = false;
        Predicate<Figure> routingExitPredicate = figure -> !(null == figure || figure.getClass().getSimpleName().equals("Knight"));
        BiPredicate<Cell, Figure> routingOccupiedCellPredicate = (cell, figure) -> !cell.equals(figure.position);

        for (Cell cell : wayToMove) {
            for (Figure figure : figures) {
                if (routingExitPredicate.test(figure)) {
                    break;
                }
                if (routingOccupiedCellPredicate.test(cell, figure)) {
                    throw new OccupiedWayException(String.format("There is a %s on the way", figure.getClass().getSimpleName()));
                }
                result = true;
            }
        }
        return result;
    }
}
