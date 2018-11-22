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
            Iterator<Integer> actual = checkNext(it) ? it.next() : null;

            @Override
            public boolean hasNext() {
                return checkNext(actual) || isNextExist();
            }

            @Override
            public Integer next() {
                if (!actual.hasNext() & !it.hasNext()) {
                    throw new NoSuchElementException("No such element");
                }
                Integer result;
                if (checkNext(actual)) {
                    result = actual.next();
                } else {
                    result = findNext();
                }
                return result;
            }

            private boolean checkNext(Iterator iterator) {
                return iterator != null && iterator.hasNext();
            }

            private Integer findNext() {
                Integer result = null;
                while (it.hasNext()) {
                    actual = it.next();
                    if (checkNext(actual)) {
                        result = actual.next();
                        break;
                    }
                }
                return result;
            }

            private boolean isNextExist() {
                boolean result = false;
                while (it.hasNext()) {
                    actual = it.next();
                    if (checkNext(actual)) {
                        result = true;
                        break;
                    }
                }
                return result;
            }
        };
    }
}