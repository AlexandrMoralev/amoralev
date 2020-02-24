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
        boolean result = (!source.equals(dest)) && destinationIsValid(dest);
        if (result) {
            result = this.board[dest.getX()][dest.getY()].tryLock()
                    || this.board[dest.getX()][dest.getY()].tryLock(Board.MOVE_TIMEOUT, TimeUnit.MILLISECONDS);
            if (result) {
                this.board[source.getX()][source.getY()].unlock();
            }
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
     * Method size - returns the size of the Board
     *
     * @return int size
     */
    public int size() {
        return this.board.length;
    }

    /**
     * Method hasQueuedThreads - queries whether other threads are waiting to take this Cell.
     *
     * @param position not-null Cell to be checked
     * @return true if there may be other threads waiting to take this Cell.
     */
    public final boolean hasQueuedThreads(final Cell position) {
        return this.board[position.getX()][position.getY()].hasQueuedThreads();
    }
}



