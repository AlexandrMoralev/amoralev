package ru.job4j.tracker;

/**
 * Imput interface
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface Input {

    String MENU_START_SEPARATOR = " ================== Menu ================== ";
    String MENU_END_SEPARATOR = " ========================================== ";
    String ACTION_LEFT_SEPARATOR = " --->> ";
    String ACTION_RIGHT_SEPARATOR = " <<--- \r\n";
    String DIVIDING_LINE = "____________________________________________________________________________________________________\r\n";
    String EXCEPT_MSG_SEPARATOR = " +++++++++++++++++++++ ";

    /**
     * Method ask
     * @param question String question to user
     * @return String answer from user
     */
    String ask(String question);

    /**
     * Method print
     * @param data String data to print
     */
    void print(String data);
}
