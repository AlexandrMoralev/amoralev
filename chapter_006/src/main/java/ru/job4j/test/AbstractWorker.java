package ru.job4j.test;

/**
 * AbstractWorker
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
abstract class AbstractWorker implements Runnable {
    private final int repeats = 10;
    private final int digit;
    private final Switch switcher;

    /**
     * AbstractWorker constructor
     *
     * @param digit    int digit to work with
     * @param switcher not-null Switch instance to work with
     */
    public AbstractWorker(final int digit, final Switch switcher) {
        this.digit = digit;
        this.switcher = switcher;
    }

    /**
     * Method acquire - acquires permission to work
     *
     * @throws InterruptedException
     */
    protected abstract void acquire() throws InterruptedException;

    /**
     * Method release - releases permission to work
     */
    protected abstract void release();

    /**
     * Method doWork - performs some useful tasks
     */
    protected void doWork() {
        for (int i = 0; i < repeats; i++) {
            this.switcher.addDigit(digit);
        }
    }

    @Override
    public void run() {
        try {
            while (this.switcher.workCounter().getAndDecrement() > 0) {
                acquire();
                doWork();
                release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int getDigit() {
        return this.digit;
    }

    public int getRepeats() {
        return this.repeats;
    }

    public Switch getSwitcher() {
        return this.switcher;
    }
}