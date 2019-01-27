package ru.job4j.test;

/**
 * Worker
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
class Worker extends AbstractWorker {
    public Worker(int digit, Switch switcher) {
        super(digit, switcher);
    }

    @Override
    protected void acquire() throws InterruptedException {
        super.getSwitcher().getEntrySemaphore().acquire();
    }

    @Override
    protected void release() {
        super.getSwitcher().getExitSemaphore().release();
    }

    @Override
    public void run() {
        super.run();
    }
}