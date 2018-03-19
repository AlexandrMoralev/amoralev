package ru.job4j.tracker;

/**
 * Imput interface
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface Input {

    // String "separators" for message printing
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

   /* /**
     * Method ask - overloaded, using for handling MenuOutException
     * @param question String question to user
     * @param range int[] range of valid values of answers
     * @return int answer from user
     */
    /*int ask(String question, int[] range);*/

    /**
     * Method print
     * @param data String data to print
     */
    void print(String data);
}
