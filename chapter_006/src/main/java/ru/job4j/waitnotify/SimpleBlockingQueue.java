package ru.job4j.waitnotify;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

/**
 * SimpleBlockingQueue
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@ThreadSafe
public class SimpleBlockingQueue<T> {

    @GuardedBy("this")
    private final Queue<T> queue;
    private final int initCapacity;
    private static final int DEFAULT_INITIAL_CAPACITY = 7;

    /**
     * Constructs SimpleBlockingQueue with default capacity
     */
    public SimpleBlockingQueue() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * SimpleBlockingQueue instance constructor
     *
     * @param capacity int capacity of the Queue, unchangeable value for the created queue
     */
    public SimpleBlockingQueue(final int capacity) {
        this.initCapacity = capacity;
        this.queue = new LinkedList<>();
    }

    /**
     * Method offer - adds the specified value to the tail of the queue.
     *
     * @param value notnull T value to add
     */
    public void offer(final T value) {
        synchronized (this) {
            validate(value);
            while (this.queue.size() == this.initCapacity) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            queue.offer(value);
            notifyAll();
        }
    }

    /**
     * Method poll - returns and removes the head of this queue.
     *
     * @return T value or null if this queue is empty
     */
    public T poll() {
        synchronized (this) {
            T result;
            while (this.queue.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            result = queue.poll();
            notify();
            return result;
        }
    }

    private void validate(final T value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Method isEmpty - checks, if the queue is empty
     *
     * @return boolean true, if this queue is empty
     * boolean false, if the queue contains elements
     */
    public synchronized boolean isEmpty() {
        return this.queue.isEmpty();
    }

    /**
     * Method size - returns number of elements in the queue
     *
     * @return int number of elements
     */
    public synchronized int size() {
        return this.queue.size();
    }

    /**
     * Method capacity - returns unchangeable capacity of this queue.
     *
     * @return int capacity
     */
    public int capacity() {
        return this.initCapacity;
    }
}
