package ru.job4j.sets;

import ru.job4j.lists.DynamicArray;

import java.util.Iterator;

/**
 * SimpleSet
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class SimpleSet<E> implements Iterable<E> {

    private DynamicArray<E> storage;

    /**
     * Constructs empty SimpleSet
     */
    public SimpleSet() {
        this.storage = new DynamicArray<>();
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
        return this.storage.iterator();
    }
}
