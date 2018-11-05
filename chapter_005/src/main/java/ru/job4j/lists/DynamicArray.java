package ru.job4j.lists;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * DynamicArray
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class DynamicArray<E> implements Iterable<E> {

    private Object[] container;
    private static final int INITSIZE = 10;
    private int position;
    private int modCount;
    private int increaseExponent;

    /**
     * DynamicArray default instance constructor
     * with initial capacity = 10.
     */
    public DynamicArray() {
        this(INITSIZE);
    }

    /**
     * DynamicArray instance constructor.
     *
     * @param size int initial size of DynamicArray.
     */
    public DynamicArray(int size) {
        if (size >= 0 && size < Integer.MAX_VALUE) {
            this.container = new Object[size];
            this.increaseExponent = size < 10 ? 4 : (int) (Math.sqrt(size) + 1);
        } else {
            this.container = new Object[0];
            this.increaseExponent = 0;
        }
        this.position = 0;
        this.modCount = 0;
    }

    /**
     * Method add - adding element to DynamicArray
     *
     * @param value E value to be added
     */
    public void add(E value) {
        if (value != null && position == this.container.length - 1) {
            increaseCapacity();
        }
        modCount++;
        this.container[position++] = value;
    }

    /**
     * Method get - returns element by index
     *
     * @param index int index of the element
     * @return E value when it's in DynamicArray,
     * null, if there is no such element
     */
    public E get(int index) {
        return (index >= 0 && index < position) ? (E) this.container[index] : null;
    }

    /**
     * Method size - returns number of non-null elements
     *
     * @return int count of elements in DynamicArray
     */
    public int size() {
        return this.position;
    }

    /**
     * Method capacity - returns capacity
     *
     * @return int capacity of DynamicArray, including empty and null elements
     */
    public int capacity() {
        return this.container.length;
    }

    /**
     * Method increaseCapacity
     */
    private void increaseCapacity() {
        Object[] newArray = new Object[((int) Math.pow(2, increaseExponent++))];
        System.arraycopy(this.container,
                0,
                newArray,
                0,
                position
        );
        this.container = newArray;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int expectedModCount = modCount;
            private int index = 0;

            @Override
            public boolean hasNext() {
                if (expectedModCount != modCount) {
                    throw new ConcurrentModificationException();
                }
                return index < position;
            }

            @Override
            public E next() {
                if (expectedModCount != modCount) {
                    throw new ConcurrentModificationException("ConcurrentModification state");
                }
                if (!hasNext()) {
                    throw new NoSuchElementException("No such element");
                }
                return (E) container[index++];
            }
        };
    }
}
