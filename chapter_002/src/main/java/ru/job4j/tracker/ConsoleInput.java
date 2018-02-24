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
     * Method print - printing input String data
     * @param data String data to print
     */
    @Override
    public void print(String data) {
        System.out.println(data);
    }
}
