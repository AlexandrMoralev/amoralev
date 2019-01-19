package ru.job4j.exam;

import java.util.ArrayList;
import java.util.List;

/**
 * Game
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Game {
    private final GameSettings settings = new GameSettings();
    private final Board board;
    private final GameObject hero;
    private final int enemiesOnBoard;
    private final List<GameObject> enemies;
    private MovementControl heroMovementControl;
    private MovementControl enemiesMovementControl;

    /**
     * Constructs Game instance with initial parameters
     *
     * @param boardSize       int size of game board, if initial size < 10, creates Board of DEFAULT_BOARD_SIZE
     * @param numberOfEnemies int number of Enemies on this Board
     * @param mode            String game mode.
     *                        Player mode - the player controls the Bomberman movement
     *                        Demo mode - Bomberman moves randomly in automatic mode
     */
    public Game(final int boardSize, final int numberOfEnemies, final String mode) {
        int size = boardSize >= GameSettings.DEFAULT_BOARD_SIZE ? boardSize : GameSettings.DEFAULT_BOARD_SIZE;
        String gameMode = mode != null ? mode : "";
        this.board = new Board(size);
        this.hero = new Bomberman(GameSettings.HERO_INIT_POSITION);
        setMovementControl(gameMode);
        this.enemiesOnBoard = calculateEnemies(numberOfEnemies);
        this.enemies = new ArrayList<>(this.enemiesOnBoard);
        placeBlocks();
    }

    private void setMovementControl(final String mode) {
        if (mode.equalsIgnoreCase("")
                || mode.equalsIgnoreCase(GameSettings.PLAYER_MODE)) {
            this.heroMovementControl = new CustomMovementControl();
            this.enemiesMovementControl = new AutoMovementControl();
        } else {
            this.heroMovementControl = new AutoMovementControl();
            this.enemiesMovementControl = new AutoMovementControl();
        }
    }

    private int calculateEnemies(final int numberOfEnemies) {
        int result = numberOfEnemies;
        if (numberOfEnemies < 0) {
            result = 0;
        }
        if (numberOfEnemies > this.board.size()) {
            result = this.board.size();
        }
        return result;
    }

    /**
     * Method startGame - creates and starts GameObject's Threads
     */
    public void startGame() {
        Thread bomberman = initMovingLogic(this.hero);
        bomberman.setName(GameSettings.HERO_NAME);
        Thread enemies = initMovingLogic(this.enemies);
        enemies.setName(GameSettings.ENEMIES_NAME);
        bomberman.start();
        enemies.start();
        try {
            Thread.sleep(GameSettings.GAME_TIMEOUT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bomberman.interrupt();
            enemies.interrupt();
            Thread.currentThread().interrupt();
        }
    }

    private void placeBlocks() {
        Block block;
        for (int i = 1; i < this.board.size(); i += 2) {
            for (int j = 1; j < this.board.size(); j += 2) {
                block = new Block(new Cell(i, j));
                this.board.add(block);
            }
        }
    }

    private void placeMonsters() {
        int counter = this.enemiesOnBoard;
        GameObject monster;
        for (int i = 2; i < this.board.size(); i += 2) {
            for (int j = 2; j < this.board.size(); j += 2) {
                if (counter == 0) {
                    break;
                }
                monster = new Enemy(new Cell(i, j));
                this.board.add(monster);
                this.enemies.add(monster);
                counter--;
            }
        }
    }

    /**
     * Method initMovingLogic - creates game-logic Thread for a single GameObject
     *
     * @param gameObject notnull GameObject
     * @return new Thread that defines the behavior of the GameObject
     */
    private Thread initMovingLogic(final GameObject gameObject) {
        return new Thread(() -> {
            this.board.add(gameObject);
            MovementControl movementControl = gameObject.toString().toLowerCase().contains("bomberman") ? heroMovementControl : enemiesMovementControl;
            Cell currentPosition;
            Cell nextPosition;
            boolean moved;
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    currentPosition = gameObject.getPosition();
                    nextPosition = movementControl.nextStep(currentPosition);
                    moved = board.move(currentPosition, nextPosition);
                    if (moved) {
                        gameObject.setPosition(nextPosition);
                    }
                    Thread.sleep(GameSettings.HERO_TIMEOUT);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    /**
     * Method initMovingLogic - creates game-logic Thread for multiple GameObjects
     *
     * @param gameObjects notnull List with gameObjects
     * @return new Thread that defines the behavior of all gameObjects
     */
    private Thread initMovingLogic(final List<GameObject> gameObjects) {
        return new Thread(() -> {
            placeMonsters();
            MovementControl movementControl = gameObjects.size() > 0
                    && gameObjects.get(0).toString().toLowerCase().contains("bomberman")
                    ? heroMovementControl
                    : enemiesMovementControl;
            Cell currentPosition;
            Cell nextPosition;
            boolean moved;
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    for (GameObject monster : gameObjects) {
                        currentPosition = monster.getPosition();
                        nextPosition = movementControl.nextStep(currentPosition);
                        moved = board.move(currentPosition, nextPosition);
                        if (moved) {
                            monster.setPosition(nextPosition);
                        }
                        Thread.sleep(GameSettings.ENEMIES_TIMEOUT);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }
}