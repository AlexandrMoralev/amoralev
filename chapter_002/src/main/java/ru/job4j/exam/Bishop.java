package ru.job4j.exam;

import java.util.function.Predicate;

/**
 * Bishop
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Bishop extends Figure {

    /**
     * Bishop instance constructor
     *
     * @param initCell initial Cell to place the Bishop
     */
    public Bishop(Cell initCell) {
        super(initCell);
    }

    /**
     * Method way - calculating moving route of the Bishop
     *
     * @param source      initial cell of the Bishop's movement
     * @param destination Bishop's movement destination Cell
     * @return Cell[] for Bishop's move
     * @throws ImpossibleMoveException when Figure can't move to dest Cell for any reason
     */
    @Override
    public Cell[] way(Cell source, Cell destination) throws ImpossibleMoveException {

        Cell[] result = route(source, destination);

        if (result.length != 0) {
            return result;
        } else {
            throw new ImpossibleMoveException(String.format("The %s can't move this way.", this.getClass().getSimpleName()));
        }
    }

    /**
     * Method copy - movement realisation by copying Bishop to new Cell
     *
     * @param dest destination Cell
     * @return new Figure at destination Cell
     */
    @Override
    public Figure copy(Cell dest) {
        return new Bishop(dest);
    }

    private Cell[] route(Cell source, Cell dest) throws ImpossibleMoveException {

        Predicate<Cell> isSameCell = cell -> cell.equals(dest);
        Predicate<Cell> isZeroCell = cell -> !cell.isNotZeroCell();

        if (isSameCell.test(source)) {
            throw new ImpossibleMoveException("The destination and source cells are the same.");
        }
        if (isZeroCell.test(source) || isZeroCell.test(dest)) {
            throw new ImpossibleMoveException("Cell is out of Board");
        }

        Cell[] result = new Cell[0];

        // using source Cell coordinates as cell counters at while() cycle
        int rank = source.getRank();
        int file = source.getFile();

        //
        int rankDelta = -Integer.compare(source.getRank(), dest.getRank());
        int fileDelta = -Integer.compare(source.getFile(), dest.getFile());

        // ! counter calculation depends on Figure behavior
        int counter = Math.abs(source.getFile() - dest.getFile());
        int position = 0;
        Cell[] cells = new Cell[counter];

        // 3. checking whether the destination Cell is on the way
        while (rank <= Board.DIMENSION & file <= Board.DIMENSION) {

            // if Bishop can reach the destination
            if (rank == dest.getRank() & file == dest.getFile()) {
                result = cells;
                break;
            }
            if (position == counter) {
                throw new ImpossibleMoveException("The Bishop can't go that way");
            }
            // this is how the Bishop moving
            rank += rankDelta;
            file += fileDelta;
            cells[position++] = new Cell(rank, file);
        }
        return result;
    }
}
