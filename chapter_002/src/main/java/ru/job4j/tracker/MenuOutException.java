package ru.job4j.tracker;

/**
 * MenuOutException
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class MenuOutException extends RuntimeException {
    /**
     * Instance constructor
     * @param msg String exception message
     */
    public MenuOutException(String msg) {
        super(msg);
    }
}
