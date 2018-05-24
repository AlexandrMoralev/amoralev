package ru.job4j.exam;

/**
 * Board
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Board {

    // number of Figures
    final static int FIGURES = 32;

    // board dimension
    final static int DIMENSION = 8;

    private Figure[] figures = new Figure[FIGURES];
    private int position = 0;

    /**
     * Method add - adds Figure on the Board
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
     * @param source initial Cell to move
     * @param destination destination Cell
     * @return true if thr Figure moved successful, false if it's not
     * @throws ImpossibleMoveException when it's impossible to move the Figure that way
     * @throws OccupiedWayException when there is other Figure on the way
     * @throws FigureNotFoundException when there is no Figure in source Cell
     */
    boolean move(Cell source, Cell destination) throws ImpossibleMoveException, OccupiedWayException, FigureNotFoundException {

        boolean result = false;
        int figurePosition;
        Cell[] wayToMove;

        // is there Figure at source Cell?
        // else FNF exception
        figurePosition = figureAtCell(source);

        if (figurePosition < FIGURES) {

            wayToMove = figures[figurePosition].way(source, destination); // throws ImpossibleMoveException()

            // is the Figure able to move to destination Cell
            // return Cell[] path or IM exception
            if (null != wayToMove) {

                // is any other Figures on the way?
                // else throws OccupiedWayException exception
                if (isWayClean(wayToMove)) {
                    this.figures[figurePosition] = this.figures[figurePosition].copy(destination);
                    result = true;
                }

            } else {
                throw new ImpossibleMoveException("The " + figures[figurePosition].getClass().getSimpleName() + " can't move to destination cell");
            }

        } else {
            throw new FigureNotFoundException("There is no figure at " + source.getRank() + "-" + source.getFile());
        }

        return result;
    }

    // returns int position of the Figure at the Board, if it's exists
    // else returns int = 33 (out of bounds)
    private int figureAtCell(Cell aCell) {

        int result = FIGURES; // init is out of array bounds
        int count = 0;

        // is there Figure at source Cell?
        // else FNF exception
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

    // returns true when way is clean, and false when it's not
    // Knight doesn't occupy way
    private boolean isWayClean(Cell[] wayToMove) throws OccupiedWayException {

        boolean result = false; // is reaches return statement

        // is any other Figures on the way? (except Knight)
        // else throws OW exception
        for (Cell cell : wayToMove) {
            for (Figure figure : figures) {
                if (null == figure || figure.getClass().getSimpleName().equals("Knight")) {
                    break;
                }
                if (!cell.equals(figure.position)) {
                    result = true;
                } else {
                    throw new OccupiedWayException("There is a figure on the way");
                }
            }
        }

        return result;
    }
}
