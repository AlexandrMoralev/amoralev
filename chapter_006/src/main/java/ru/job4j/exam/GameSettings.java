package ru.job4j.exam;

/**
 * GameSettings
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public final class GameSettings {
    public final static int GAME_TIMEOUT = 15_000;
    public final static int HERO_TIMEOUT = 1_000;
    public final static int ENEMIES_TIMEOUT = 500;
    public final static int DEFAULT_BOARD_SIZE = 10;
    public final static String HERO_NAME = "bomberman";
    public final static String ENEMIES_NAME = "enemies";
    public final static Cell HERO_INIT_POSITION = new Cell(0, 0);
    public final static String DEMO_MODE = "demo";
    public final static String PLAYER_MODE = "player";
}
