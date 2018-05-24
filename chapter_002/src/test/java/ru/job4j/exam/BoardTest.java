package ru.job4j.exam;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;

/**
 * BoardTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class BoardTest {

    /**
     * Test. Adding more than 32 Figures to the Board.
     */
    @Test
    public void whenAdding33FiguresToBoardThenPrintConsoleMsg() {

        // std SyStem.out reference
        final PrintStream stdOut = System.out;
        // "buffered" output for tests
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        Board board = new Board();

        // error message, when trying to add more than 32 Figures to the Board
        String errMsg = "Board is already filled with pawns and pieces. You cannot add more figures to the Board.";

        System.setOut(new PrintStream(out));

        // adding 32 Bishops to the Board
        for (int i = 1; i <= Board.DIMENSION; i++) {
            for (int j = 1; j <= Board.DIMENSION / 2; j++) {
                board.add(new Bishop(new Cell(i, j)));
            }
        }

        // adding 33-rd Bishop to the Board
        board.add(new Bishop(new Cell(8, 8)));

        // if out contains errNsg then test passed
        assertThat(out.toString().contains(errMsg), is(true));

        System.setOut(stdOut);
    }

    /**
     * Test. Moving Figure.
     */
    @Test
    public void whenMovingFigureThenFigureAtNewLocation() {

        Board board = new Board();

        Cell init = new Cell(Cell.F, 1);
        Cell dest = new Cell(Cell.D, 3);

        Figure figure = new Bishop(init);

        board.add(figure);

        try {
            board.move(init, dest);
        } catch (ImpossibleMoveException | OccupiedWayException | FigureNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test. Moving a non-existent Figure.
     */
    @Test
    public void whenMovingFigureThatDoesNotExistThenFNFException() {

        String errMsg = "There is no figure at "; // FigureNotFoundException message
        Board board = new Board();

        try {
            board.move(new Cell(Cell.F, 1), new Cell(Cell.D, 3));
        } catch (ImpossibleMoveException | OccupiedWayException e) {
            System.out.println(e.getMessage());
        } catch (FigureNotFoundException e) {
            assertThat(e.getMessage(), containsString(errMsg));
        }
    }

    /**
     * Test. Figure moving the incorrect way
     */
    @Test
    public void whenMovingFigureWrongWayThenIMException() {

        String errMsg = "can't go that way"; // ImpossibleMoveException message
        Board board = new Board();
        Figure figure = new Bishop(new Cell(Cell.F, 1));

        board.add(figure);

        try {
            board.move(figure.position, new Cell(Cell.E, 1));
        } catch (ImpossibleMoveException e) {
            assertThat(e.getMessage(), containsString(errMsg));
        } catch (OccupiedWayException | FigureNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test. Figure moving outside of Board
     */
    @Test
    public void whenMovingFigureOutOfBoardThenIMException() {

        String errMsg = "Cell is out of Board";

        Board board = new Board();
        Figure figure = new Bishop(new Cell(Cell.F, 1));

        board.add(figure);

        try {
            board.move(figure.position, new Cell(19, 0));
        } catch (ImpossibleMoveException e) {
            assertThat(e.getMessage(), containsString(errMsg));
        } catch (OccupiedWayException | FigureNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test. When other Figure on the way (also when Figure moving to occupied Cell)
     */
    @Test
    public void whenMovingWayOccupiedByOtherFigureThenOWException() {

        String errMSg = "There is a figure on the way";

        Board board = new Board();

        Bishop bishopF1 = new Bishop(new Cell(Cell.F, 1));
        Bishop bishopE2 = new Bishop(new Cell(Cell.E, 2));

        board.add(bishopF1);
        board.add(bishopE2);

        try {
            board.move(bishopF1.position, bishopE2.position);
        } catch (OccupiedWayException e) {
            assertThat(e.getMessage(), containsString(errMSg));
        } catch (ImpossibleMoveException | FigureNotFoundException e) {
            e.printStackTrace();
        }
    }
}
