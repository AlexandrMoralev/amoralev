package ru.job4j.test;

import java.util.concurrent.CountDownLatch;

/*
 * Нужно написать программу, которая всегда падает в дедлок.
 * в программе нельзя использовать метод sleep.
 */

/**
 * DeadLock
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class DeadLock {
    private final String first = "first";
    private final String second = "second";
    private CountDownLatch latch = new CountDownLatch(4);
    private volatile String result;

    private final Runnable directBlock = () -> {
        synchronized (first) {
            result = first;
            latch.countDown();
            synchronized (second) {
                result += second;
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result = "forwardBlock";
            }
        }
    };

    private final Runnable reversedBlock = () -> {
        synchronized (second) {
            result = second;
            latch.countDown();
            synchronized (first) {
                result += first;
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result = "reversedBlock";
            }
        }
    };

    public String getResult() {
        return this.result;
    }

    public Runnable getDirectBlock() {
        return this.directBlock;
    }

    public Runnable getReversedBlock() {
        return this.reversedBlock;
    }

    public CountDownLatch getLatch() {
        return this.latch;
    }
}
