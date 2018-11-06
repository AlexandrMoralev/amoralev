package ru.job4j.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * EvenArrayIterator
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class EvenArrayIterator implements Iterator {

    private int[] array;
    private int position;

    public EvenArrayIterator(final int[] numbers) {
        this.array = numbers;
        this.position = 0;
    }

    @Override
    public boolean hasNext() {
        return this.iterate() != -1;
    }

    @Override
    public Integer next() {
        if (this.position == -1) {
            throw new NoSuchElementException("No more even elements");
        }
        int result = this.array[position++];
        return result;
    }

    private int iterate() {
        int result = -1;
        for (int i = position; i < this.array.length; i++) {
            if (this.array[i] % 2 == 0) {
                position = i;
                result = i;
                break;
            }
        }
        return result;
    }
}
