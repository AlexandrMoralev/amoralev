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

    // named columns of the board
    final static int A = 1;
    final static int B = 2;
    final static int C = 3;
    final static int D = 4;
    final static int E = 5;
    final static int F = 6;
    final static int G = 7;
    final static int H = 8;

    private int file; // == row
    private int rank; // == column

    /**
     * Cell instance constructor
     * when rank | file is incorrect, fields initialises with int 0
     * and must be checked by isNotZeroCell()
     *
     * @param rank int initial column
     * @param file int initial row
     */
    public Cell(int rank, int file) {

        if (rank >= Cell.A & rank <= Board.DIMENSION) {
            if (file > 0 & file <= Board.DIMENSION) {
                this.rank = rank;
                this.file = file;
            }
        }
    }

    /**
     * Method getFile
     *
     * @return int Cell's row
     */
    public int getFile() {
        return this.file;
    }

    /**
     * Method getRank
     *
     * @return int Cell's column
     */
    public int getRank() {
        return this.rank;
    }

    /**
     * Method isNotZeroCell - checks default Cell coordinates zero-initialization
     *
     * @return true when all right, false if initial Cell coordinates is out of Board
     */
    public boolean isNotZeroCell() {
        return (this.file != 0 & this.rank != 0);
    }

    /**
     * Method equals
     *
     * @param o Cell to compare
     * @return true when Cells are equal, false when Cells are different
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cell cell = (Cell) o;
        return file == cell.file && rank == cell.rank;
    }

    /**
     * Method hashCode
     *
     * @return int hashCode, calculated by file and rank fields
     */
    @Override
    public int hashCode() {
        return Objects.hash(file, rank);
    }
}
