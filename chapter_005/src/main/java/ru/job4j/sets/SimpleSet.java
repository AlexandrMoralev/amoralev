package ru.job4j.sets;

import ru.job4j.lists.DynamicArray;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * SimpleSet
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class SimpleSet<E> implements Iterable<E> {

    private DynamicArray<E> storage;
    private int modCounter;

    /**
     * Constructs empty SimpleSet
     */
    public SimpleSet() {
        this.storage = new DynamicArray<>();
        this.modCounter = 0;
    }

    /**
     * Method add - adds unique E element to this set
     *
     * @param e E non-null element
     */
    public void add(E e) {
        if (e == null) {
            throw new IllegalArgumentException();
        }
        if (isAbsent(e)) {
            storage.add(e);
            modCounter++;
        }
    }

    /**
     * Method size - returns number of elements in this set
     *
     * @return int size
     */
    public int size() {
        return this.storage.size();
    }

    private boolean isAbsent(E e) {
        boolean result = true;
        for (E item : storage) {
            if (e.equals(item)) {
                result = false;
                break;
            }
        }
        return result;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int counter = 0;
            private int modifications = modCounter;

            @Override
            public boolean hasNext() {
                isModified();
                return counter != storage.size();
            }

            @Override
            public E next() {
                if (storage.size() == 0) {
                    throw new NoSuchElementException();
                }
                isModified();
                return storage.get(counter++);
            }

            private void isModified() {
                if (modifications != modCounter) {
                    throw new ConcurrentModificationException("ConcurrentModification state");
                }
            }
        };
    }
}
