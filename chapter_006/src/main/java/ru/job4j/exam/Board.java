package ru.job4j.exam;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Board
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Board {
    private final ReentrantLock[][] board;
    private final static int MOVE_TIMEOUT = 500;

    /**
     * Constructs Board of initial size
     *
     * @param size int initial size
     */
    public Board(final int size) {
        this.board = new ReentrantLock[size][size];
        initBoard(size);
    }

    private void initBoard(int initSize) {
        for (int i = 0; i < initSize; i++) {
            for (int j = 0; j < initSize; j++) {
                this.board[i][j] = new ReentrantLock();
            }
        }
    }

    /**
     * Method move - tryLock destination Cell on this.board for 500ms,
     * when dest locked unlocks the source Cell
     *
     * @param source notnull Cell to move from
     * @param dest   notnull Cell to be moved
     * @return boolean true, if moved successfully
     * false, otherwise
     * @throws InterruptedException when Thread was interrupted during the MOVE_TIMEOUT
     */
    public boolean move(final Cell source, final Cell dest) throws InterruptedException {
        validateInput(source, dest);
        boolean result = (!source.equals(dest)) & destinationIsValid(dest);
        if (result) {
            result = this.board[dest.getX()][dest.getY()].tryLock(Board.MOVE_TIMEOUT, TimeUnit.MILLISECONDS);
            this.board[source.getX()][source.getY()].unlock();
        }
        return result;
    }

    private void validateInput(final Object arg) {
        if (null == arg) {
            throw new IllegalArgumentException();
        }
    }

    private void validateInput(final Object... arg) {
        for (Object o : arg) {
            validateInput(o);
        }
    }

    private boolean destinationIsValid(final Cell dest) {
        return dest.getX() >= 0
                & dest.getX() < this.board.length
                & dest.getY() >= 0
                & dest.getY() < this.board.length;
    }

    /**
     * Method add - adds GameObject on this Board, by locking cell at this Board
     *
     * @param gameObject notnull GameObject to be placed on the Board
     */
    public void add(final GameObject gameObject) {
        this.board[gameObject.getPosition().getX()][gameObject.getPosition().getY()].lock();
    }

    /**
     * Method randomNearbyCell - randomly generates the adjacent cell
     *
     * @param source notnull Cell for generation of nearby source Cell
     * @return randomly generated Cell.
     */
    public Cell randomNearbyCell(final Cell source) {
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

    /**
     * Method size - returns the size of the Board
     *
     * @return int size
     */
    public int size() {
        return this.board.length;
    }
}



