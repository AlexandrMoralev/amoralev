package ru.job4j.exam;

import org.junit.jupiter.api.Test;

/**
 * GameTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class GameTest {

    @Test
    public void whenBoardSizeThenDefaultBoard() {
        new Game(-1, 0, "wtf").startGame();
    }

    @Test
    public void whenBoard1000ThenGameRuns() {
        new Game(1000, 0, null).startGame();
    }

    @Test
    public void whenPlaceIncorrectNumberOfMonstersThenGameRunsWithoutMonsters() {
        new Game(10, -2, "").startGame();
    }

    @Test
    public void whenStartingPlayerModeThenGameRunsPlayerMode() {
        new Game(100, 100, GameSettings.PLAYER_MODE).startGame();
    }

    @Test
    public void whenStartingDemoModeThenGameRunsDemoMode() {
        new Game(100, 100, GameSettings.DEMO_MODE).startGame();
    }
}
