package ru.job4j.tracker;

/**
 * Imput interface
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface Input {
    /**
     * Method ask
     * @param question String question to user
     * @return String answer from user
     */
    String ask (String question);

    /**
     * Method print
     * @param data String data to print
     */
    void print(String data);
}
