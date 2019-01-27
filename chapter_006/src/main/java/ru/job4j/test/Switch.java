package ru.job4j.test;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * 1) Реализуйте объект, который хранит в себе строку или ее представление. Объекту необходимо:
 * - содержать метод, который принимает на вход значение типа int,
 * конвертирует его в строковое представление (например, 4 -> "4"), а затем добавляет к концу строки.
 * - по требованию возвращать эту строку.
 * 2) Реализуйте 2 потока, которые в цикле добавляют всегда одно и то же число (1-й поток число 1, второй поток число 2) в строку из пункта 1.
 * Работа потоков должна быть организована таким образом, чтобы числа добавлялись в строку в следующем порядке:
 * сначала 10 чисел из первого потока, затем 10 чисел из второго, затем снова 10 чисел из первого и так далее.
 */

/**
 * Switch
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Switch {
    private final StringBuilder stringBuilder;
    private final SwitchControl switchControl;
    private final AtomicInteger switchesToDo;

    /**
     * Constructs Switch instance
     *
     * @param switchesToDo int number of the switches to perform
     */
    public Switch(final int switchesToDo) {
        this.switchesToDo = new AtomicInteger(switchesToDo);
        this.stringBuilder = new StringBuilder();
        this.switchControl = new SwitchControl();
    }

    /**
     * Method addDigit - converts input digit in a String representation
     * and appends it to the internal String value, using StringBuilder
     *
     * @param digit int digit to be added
     */
    public void addDigit(final int digit) {
        this.stringBuilder.append(digit);
    }

    /**
     * Method getValue
     *
     * @return String representation of all added digits
     */
    public String getValue() {
        return this.stringBuilder.toString();
    }

    /**
     * Method workCounter
     *
     * @return AtomicInteger amount of work performed
     */
    public AtomicInteger workCounter() {
        return this.switchesToDo;
    }

    /**
     * Method getEntrySemaphore
     *
     * @return Semaphore to start working
     */
    public Semaphore getEntrySemaphore() {
        return this.switchControl.getEntrySemaphore();
    }

    /**
     * Method getExitSemaphore
     *
     * @return Semaphore to complete the work
     */
    public Semaphore getExitSemaphore() {
        return this.switchControl.getExitSemaphore();
    }

    private class SwitchControl {
        private final int permits = 1;
        private volatile Semaphore entrySemaphore;
        private volatile Semaphore exitSemaphore;

        private SwitchControl() {
            this.entrySemaphore = new Semaphore(this.permits);
            this.exitSemaphore = new Semaphore(0);
        }

        private Semaphore getEntrySemaphore() {
            return this.entrySemaphore;
        }

        private Semaphore getExitSemaphore() {
            return this.exitSemaphore;
        }
    }
}
