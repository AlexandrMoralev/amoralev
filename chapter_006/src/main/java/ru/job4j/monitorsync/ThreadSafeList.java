package ru.job4j.monitorsync;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import ru.job4j.lists.DynamicArray;

import java.util.Iterator;

/**
 * ThreadSafeList
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@ThreadSafe
public class ThreadSafeList<T> implements Iterable<T> {

    @GuardedBy("this")
    private DynamicArray<T> collection;

    public ThreadSafeList() {
        this.collection = new DynamicArray<>();
    }

    public ThreadSafeList(int initCapacity) {
        this.collection = new DynamicArray<>(initCapacity);
    }

    public synchronized void add(final T value) {
        this.collection.add(value);
    }

    public synchronized T get(final int index) {
        return index >= 0 ? this.collection.get(index) : null;
    }

    public synchronized int size() {
        return this.collection.size();
    }

    public synchronized Iterator<T> iterator() {
        return copy(this.collection).iterator();
    }

    private DynamicArray<T> copy(final DynamicArray<T> collection) {
        final DynamicArray<T> duplicate = new DynamicArray<>(collection.size());
        for (T element : collection) {
            duplicate.add(element);
        }
        return duplicate;
    }
}
