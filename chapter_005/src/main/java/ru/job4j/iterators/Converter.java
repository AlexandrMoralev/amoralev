package ru.job4j.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Converter
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Converter {
    Iterator<Integer> convert(Iterator<Iterator<Integer>> it) {
        return new Iterator<Integer>() {
            Iterator<Integer> actual = (it != null && it.hasNext()) ? it.next() : null;

            boolean isActual;
            boolean isIterable;
            boolean hasNext;
            int iteration = 0;

            @Override
            public boolean hasNext() {
                check();
                return hasNext;
            }

            @Override
            public Integer next() {
                check();
                if (!hasNext) {
                    throw new NoSuchElementException("No such element");
                }
                if (!isActual) {
                    actual = it.next();
                }
                iteration++;
                return actual.next();
            }

            private void check() {
                isActual = actual != null & actual.hasNext();
                isIterable = it.hasNext() & iteration != 0;
                hasNext = isActual || isIterable;
            }
        };
    }
}