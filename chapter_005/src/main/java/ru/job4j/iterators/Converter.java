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
            Iterator actual = it.next();

            @Override
            public boolean hasNext() {
                return actual.hasNext() || it.next().hasNext();
            }

            @Override
            public Integer next() {
                if (!(it.hasNext() && actual.hasNext())) {
                    throw new NoSuchElementException("No such element");
                }
                Integer result = null;
                if (actual.hasNext()) {
                    result = (Integer) actual.next();
                } else {
                    actual = it.next();
                }
                return result;
            }
        };
    }
}