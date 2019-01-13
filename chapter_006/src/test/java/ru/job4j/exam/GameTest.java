package ru.job4j.exam;

import org.junit.Test;

/**
 * GameTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class GameTest {
    private final Game game = new Game(10);

    @Test
    public void whenThen() {
        game.startGame();
    }
}
