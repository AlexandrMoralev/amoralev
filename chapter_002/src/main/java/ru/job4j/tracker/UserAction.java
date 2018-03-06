package ru.job4j.tracker;

/**
 * UserAction interface
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface UserAction {

    int key();

    void execute(Input input, Tracker tracker);

    String info();
}
