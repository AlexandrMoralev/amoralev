package ru.job4j.tracker;

/**
 * ValidateInput
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class ValidateInput extends ConsoleInput {
    /**
     * Method ask - checking whether the chosen menu item is in range of values
     * @param question String question to user
     * @param range String[] range of valid values of answers
     * @return validated int value
     */
    @Override
    public int ask(String question, int[] range) {
        boolean invalidData = true;
        int value = -1;
        do {
            try {
                value = super.ask(question, range);
                invalidData = false;
            } catch (MenuOutException moe) {
                System.out.println("Please select key from the menu. ");
            } catch (NumberFormatException nfe) {
                System.out.println("Please enter valid data. ");
            }
        } while (invalidData);

        return value;
    }
}
