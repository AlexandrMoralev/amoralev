package ru.job4j.generics;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * SimpleArray
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class SimpleArray<T> implements Iterable<T> {

    private Object[] array;
    private int position;

    public SimpleArray(int size) {
        this.array = size > 0 ? new Object[size] : new Object[0];
        position = 0;
    }

    public void add(T model) {
        if (position == this.array.length) {
            throw new ArrayIndexOutOfBoundsException("Array size reached");
        }
        this.array[position++] = model;
    }

    public boolean set(int index, T model) {
        boolean result = index >= 0 && index < position;
        if (result) {
            this.array[index] = model;
        }
        return result;
    }

    public boolean delete(int index) {
        boolean result = index >= 0 && index < position;
        if (result) {
            System.arraycopy(this.array,
                    index + 1,
                    this.array, index,
                    position - index - 2);
        }
        return result;
    }

    public T get(int index) {
        if (index < 0 || index >= position) {
            throw new NoSuchElementException("No such element");
        }
        return (T) this.array[index];
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int index = 0;
            @Override
            public boolean hasNext() {
                return index < position;
            }

            @Override
            public T next() {
                return get(index++);
            }
        };
    }
}
