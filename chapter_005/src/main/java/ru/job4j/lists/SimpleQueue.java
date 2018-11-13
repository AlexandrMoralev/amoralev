package ru.job4j.lists;

import java.util.NoSuchElementException;

/**
 * SimpleQueue
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class SimpleQueue<T> {

    private SimpleStack<T> receiver;
    private SimpleStack<T> supplier;
    private int size;

    /**
     * Constructs empty SimpleQueue
     */
    public SimpleQueue() {
        this.receiver = new SimpleStack<T>();
        this.supplier = new SimpleStack<T>();
        this.size = 0;
    }

    /**
     * Method poll - returns and deletes the first element of this queue
     *
     * @return T element
     */
    public T poll() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        if (this.supplier.size() == 0) {
            while (this.receiver.size() > 0) {
                T poll = receiver.poll();
                supplier.push(poll);
            }
        }
        size--;
        return this.supplier.poll();
    }

    /**
     * Method push - adds T value to this queue
     *
     * @param value T
     */
    public void push(T value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }
        size++;
        this.receiver.push(value);
    }

    /**
     * Method size
     *
     * @return int number of elements in this queue
     */
    public int size() {
        return this.size;
    }
}
