package ru.job4j.tracker;

/**
 * StubInput
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class StubInput implements Input {
    private final String[] value;
    private int position;

    /**
     * StubInput instance constructor
     * @param value String[] of User answers
     */
    StubInput(final String[] value) {
        this.value = value;
    }

    /**
     * Method ask
     * @param question String question to user
     * @return String answer from value array by position
     */
    @Override
    public String ask(String question) {
        return this.value[this.position++];
    }

    /**
     * Method ask (when validating menu selection key)
     * @param question String question to user
     * @param range String[] range of valid values of answers
     * @return
     */
    @Override
    public int ask(String question, int[] range) {
        //throw new UnsupportedOperationException("Unsupported operation");
        return Integer.valueOf(this.ask(question));
    }

    /**
     * Method print
     * @param data String data to print
     */
    @Override
    public void print(String data) {
        System.out.println(data);
    }
}
