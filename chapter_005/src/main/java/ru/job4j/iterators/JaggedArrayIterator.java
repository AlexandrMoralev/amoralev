package ru.job4j.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * JaggedArrayIterator
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class JaggedArrayIterator implements Iterator {

    private int[][] array;
    private int row;
    private int column;

    public JaggedArrayIterator(int[][] arr) {
        this.array = arr != null ? arr : new int[0][0];
        this.row = 0;
        this.column = 0;
    }

    @Override
    public boolean hasNext() {
        boolean result;
        if (row >= this.array.length - 1 && column >= this.array[row].length) {
            result = false;
        } else {
            result = row < this.array.length || column < this.array[row].length;
        }
        return result;
    }

    @Override
    public Integer next() {
        if (row == this.array.length - 1 && column == this.array[row].length) {
            throw new NoSuchElementException("No such element");
        }
        Integer result = this.array[row][column];
        if (row < this.array.length - 1 && column == this.array[row].length - 1) {
            row++;
            column = 0;
        } else {
            column++;
        }
        return result;
    }
}
