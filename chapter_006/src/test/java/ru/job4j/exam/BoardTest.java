package ru.job4j.exam;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * BoardTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class BoardTest {
    private Board board;
    private Cell dest;
    private GameObject bomberman;

    @Before
    public void init() {
        board = new Board(GameSettings.DEFAULT_BOARD_SIZE);
        bomberman = new Bomberman(GameSettings.HERO_INIT_POSITION);
        board.add(bomberman);
    }

    @Test
    public void whenGameObjectMovesToValidDestinationThenMoved() throws InterruptedException {
        dest = new Cell(1, 0);
        assertThat(board.move(bomberman.getPosition(), dest), is(true));
    }

    @Test
    public void whenGameObjectMovesToSameDestinationThenMoveReturnsFalse() throws InterruptedException {
        dest = new Cell(0, 0);
        assertThat(board.move(bomberman.getPosition(), dest), is(false));
        assertThat(bomberman.getPosition(), is(dest));
    }

    @Test
    public void whenGameObjectMovesOutOfBoardThenMoveReturnsFalse() throws InterruptedException {
        dest = new Cell(board.size() - 1, board.size() + 1);
        assertThat(board.move(bomberman.getPosition(), dest), is(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenCellsAreNullMoveMethodShouldThrowIAException() throws InterruptedException {
        board.move(null, dest);
    }
}
