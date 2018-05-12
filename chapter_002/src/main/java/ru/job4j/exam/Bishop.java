package ru.job4j.exam;

/**
 * Bishop
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Bishop extends Figure {

    // int[] sign multipliers for way calculation
    // wayMask[0] is board rank
    // wayMask[1] is board file
    private final int[] wayMask = new int[2];

    /**
     * Bishop instance constructor
     * @param initCell initial Cell to place the Bishop
     */
    public Bishop(Cell initCell) {
        super(initCell);
    }

    /**
     * Method way - calculating moving route of the Bishop
     * @param source initial cell of the Bishop's movement
     * @param destination Bishop's movement destination Cell
     * @return Cell[] for Bishop's move
     * @throws ImpossibleMoveException
     */
    @Override
    public Cell[] way(Cell source, Cell destination) throws ImpossibleMoveException {

        checkDirection(source, destination);

        if (isPossibleToMove(source, destination)) {
            return route(source, destination);
        } else {
            throw new ImpossibleMoveException("The " + this.getClass().getSimpleName() + " can't move this way.");
        }
    }

    /**
     * Method copy - movement realisation by copying Bishop to new Cell
     * @param dest destination Cell
     * @return new Figure at destination Cell
     */
    @Override
    public Figure copy(Cell dest) {
        return new Bishop(dest);
    }

    // checking Figure move direction to destination
    // filling int[] wayMask, containing sign coefficients, to calculate way() correctly
    private void checkDirection(Cell source, Cell dest) {

        if (source.getRank() < dest.getRank()) {
            wayMask[0] = 1;
        } else if (source.getRank() == dest.getRank()) {
            wayMask[0] = 0;
        } else if (source.getRank() > dest.getRank()) {
            wayMask[0] = -1;
        }

        if (source.getFile() < dest.getFile()) {
            wayMask[1] = 1;
        } else if (source.getFile() == dest.getFile()) {
            wayMask[1] = 0;
        } else if (source.getFile() > dest.getFile()) {
            wayMask[1] = -1;
        }
    }

    // checking move ability to dest Cell
    private boolean isPossibleToMove(Cell source, Cell dest) throws ImpossibleMoveException {

        boolean result = false;

        // using source Cell coordinates as cell counters at while() cycle
        int rank = source.getRank();
        int file = source.getFile();

        // checking move ability
        // 1. if source Cell != destination Cell then figure can move
        if (!source.equals(dest)) {

            // 2. checking the direction of the moving figure
            if (!(wayMask[0] == 0 | wayMask[1] == 0)) {

                // 3. if Cells is within Board (zero-Cell checking)
                if (source.isNotZeroCell() & dest.isNotZeroCell()) {

                    // 4. checking whether the destination Cell is on the way
                    while (rank <= Board.DIMENSION & file <= Board.DIMENSION) {

                        // if Bishop can reach the destination
                        if (rank == dest.getRank() & file == dest.getFile()) {
                            result = true;
                            break;
                        }
                        // this is how the Bishop moving
                        rank += wayMask[0];
                        file += wayMask[1];
                    }
                } else {
                    throw new ImpossibleMoveException("Cell is out of Board");
                }
            } else {
                throw new ImpossibleMoveException("Bishop can't go this way.");
            }
        } else {
            throw new ImpossibleMoveException("The destination and source cells are the same.");
        }

        return result;
    }

    // fills in Cell[] when isPossibleToMove == true
    private Cell[] route(Cell source, Cell dest) {

        // ! counter calculation depends on Figure behavior
        int counter = Math.abs(source.getFile() - dest.getFile());

        int rank = source.getRank();
        int file = source.getFile();

        Cell[] result = new Cell[counter];

        for (int i = 0; i < counter; i++) {
            rank += wayMask[0];
            file += wayMask[1];
            result[i] = new Cell(rank, file);
        }

        return result;
    }
}
