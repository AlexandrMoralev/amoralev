package ru.job4j.tracker;

import java.util.Scanner;

/**
 * ConsoleInput
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class ConsoleInput implements Input {

    private Scanner scanner = new Scanner(System.in);

    /**
     * Method ask - asks to user for action
     * @param question String question to user
     * @return String answer from user
     */
    @Override
    public String ask(String question) {
        System.out.println(" " + question);
        return scanner.nextLine();
    }

    /**
     * Method ask - asks to user for action, then validates input
     * @param question String question to user
     * @param range int[] range of valid values of answers
     * @return validated int value
     */
    @Override
    public int ask(String question, int[] range) {
        int key = Integer.valueOf(this.ask(question));
        if (key >= 0 && key < range.length) {
            return key;
        } else {
            throw new MenuOutException("Out of menu range");
        }
    }

    /**
     * Method print - printing input String data
     * @param data String data to print
     */
    @Override
    public void print(String data) {
        System.out.println(data);
    }
}
