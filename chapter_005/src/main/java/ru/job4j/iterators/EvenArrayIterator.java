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
        return this.iterate() != null;
    }

    @Override
    public Integer next() {
        if (!this.hasNext()) {
            throw new NoSuchElementException("No more even elements");
        }
        Integer result = this.iterate();
        this.position++;
        return result;
    }

    private Integer iterate() {
        Integer result = null;
        for (int i = position; i < this.array.length; i++) {
            if (this.array[i] % 2 == 0) {
                result = this.array[i];
                break;
            }
        }
        return result;
    }
}
