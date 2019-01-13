package ru.job4j.exam;

/**
 * Game
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Game {
    private final Board board;
    private final GameObject hero;
    private final static int DEFAULT_INIT_SIZE = 10;

    /**
     * Constructs Game instance with initial Board size
     * if initial size < 10, creates Board of DEFAULT_INIT_SIZE
     *
     * @param boardSize int board size
     */
    public Game(final int boardSize) {
        int size = boardSize >= DEFAULT_INIT_SIZE ? boardSize : DEFAULT_INIT_SIZE;
        this.board = new Board(size);
        this.hero = new Bomberman(size / 2, size / 2);
    }

    /**
     * Method startGame - creates and starts GameObject's Threads
     */
    public void startGame() {
        Thread bomberman = initMovingLogic(hero);
        bomberman.setName("bomberman");
        bomberman.start();
        try {
            Thread.sleep(5_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bomberman.interrupt();
            Thread.currentThread().interrupt();
        }
    }

    private Thread initMovingLogic(final GameObject gameObject) {
        return new Thread(() -> {
            this.board.add(gameObject);
            Cell currentPosition;
            Cell nextPosition;
            boolean moved;
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    currentPosition = gameObject.getPosition();
                    nextPosition = board.randomNearbyCell(currentPosition);
                    moved = board.move(currentPosition, nextPosition);
                    if (moved) {
                        gameObject.setPosition(nextPosition);
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException | IllegalMonitorStateException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }
}
