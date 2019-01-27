package ru.job4j.test;

/**
 * CoWorker
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
class CoWorker extends AbstractWorker {
    public CoWorker(int digit, Switch switcher) {
        super(digit, switcher);
    }

    @Override
    protected void acquire() throws InterruptedException {
        super.getSwitcher().getExitSemaphore().acquire();
    }

    @Override
    protected void release() {
        super.getSwitcher().getEntrySemaphore().release();
    }

    @Override
    public void run() {
        super.run();
    }
}