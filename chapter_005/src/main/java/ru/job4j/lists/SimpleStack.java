package ru.job4j.lists;

import java.util.NoSuchElementException;

/**
 * SimpleStack
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class SimpleStack<T> {

    private DynamicList<T> list;
    private int size;

    /**
     * Constructs empty SimpleStack
     */
    public SimpleStack() {
        this.list = new DynamicList<>();
        this.size = 0;
    }

    /**
     * Method poll - return T value and delete it from this stack
     *
     * @return T value
     */
    public T poll() {
        if (this.size == 0) {
            throw new NoSuchElementException("Stack is empty");
        }
        T result = this.list.getFirst();
        this.list.deleteFirst();
        size--;
        return result;
    }

    /**
     * Method push - add T value to the top of the stack
     *
     * @param value T value
     */
    public void push(T value) {
        this.list.addFirst(value);
        size++;
    }

    /**
     * Method size - returns number of elements in this stack
     *
     * @return int size
     */
    public int size() {
        return this.size;
    }
}
