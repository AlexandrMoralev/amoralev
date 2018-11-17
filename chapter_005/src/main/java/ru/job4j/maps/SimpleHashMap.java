package ru.job4j.maps;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * SimpleHashMap
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class SimpleHashMap<K, V> {

    private Bucket[] table;
    private int size;

    private static final float FILLRATIO = 0.75f;
    private static final int INITLENGTH = 16;
    private int modified;

    /**
     * Constructs SimpleHashMap instance, with table length = 16
     */
    public SimpleHashMap() {
        this(INITLENGTH);
    }

    /**
     * SimpleHashMap instance constructor
     * @param initSize int initial table length
     */
    public SimpleHashMap(int initSize) {
        this.table = initSize >= 0 ? new Bucket[initSize] : new Bucket[0];
        this.size = 0;
        this.modified = 0;
    }

    /**
     * Method insert - adds a key-value pair to this table
     * @param key non-null key
     * @param value value
     * @return boolean true, when pair added
     *                 false, when collision occurred in this table
     */
    public boolean insert(K key, V value) {
        validate(key);
        boolean result = hasntCollision(key);
        if (result) {
            checkTableFill();
            Bucket<K, V> bucket = new Bucket<>(key, value);
            this.table[bucket.reIndex()] = bucket;
            this.size++;
            this.modified++;
        }
        return result;
    }

    /**
     * Method get - returns pair's value, by the key
     * @param key non-null key
     * @return V value, when this key-value pair is in the table
     */
    public V get(K key) {
        validate(key);
        int index = getIndex(key);
        if (index >= this.table.length || index < 0) {
            throw new NoSuchElementException();
        }
        return this.table[index] == null ? null : (V) this.table[index].getValue();
    }

    /**
     * Method delete - removes K-V pair, by the key
     * @param key non-null K key
     * @return boolean true, when pair deleted successfully
     *                 false, when there is no pair with this key in the table
     */
    public boolean delete(K key) {
        validate(key);
        int index = getIndex(key);
        boolean result = index < this.table.length
                && this.size >= 0
                && this.table[index] != null;
        if (result) {
            this.table[index] = null;
            this.size--;
            this.modified++;
        }
        return result;
    }

    /**
     * Method getSize
     * @return number of pairs in the table
     */
    public int getSize() {
        return this.size;
    }

    private void validate(Object parameter) {
        if (parameter == null) {
            throw new IllegalArgumentException();
        }
    }

    private int getIndex(K key) {
        return Math.abs(Objects.hashCode(key)) % this.table.length;
    }

    private boolean hasntCollision(K key) {
        return this.table[getIndex(key)] == null;
    }

    private void checkTableFill() {
        if (this.isFull()) {
            increaseTable();
        }
    }

    private boolean isFull() {
        return this.size >= this.table.length * FILLRATIO - 1;
    }

    private void increaseTable() {
        int newLength = this.table.length * 3 / 2 + 1;
        Bucket[] newTable = new Bucket[newLength];
        this.fillTable(newTable);
        this.table = newTable;
        this.modified++;
    }

    private void fillTable(Bucket[] newTable) {
        for (Bucket bucket : table) {
            if (bucket != null && bucket.getKey() != null) {
                newTable[bucket.reIndex()] = bucket;
            }
        }
    }

    public Iterator<Bucket> iterator() {
        return new Iterator<Bucket>() {
            int expectedModCount = modified;
            int position = 0;
            int itemsTaken = 0;

            @Override
            public boolean hasNext() {
                isModified();
                return itemsTaken < size;
            }

            @Override
            public Bucket next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Bucket result = null;
                for (int i = position; i < table.length; i++) {
                    if (table[i] != null) {
                        itemsTaken++;
                        result = table[i];
                        position++;
                        break;
                    }
                }
                return result;
            }

            private void isModified() {
                if (expectedModCount != modified) {
                    throw new ConcurrentModificationException();
                }
            }
        };
    }

    /**
     * Buscket - storage of K-V pair
     * @param <K> non-null key
     * @param <V> value
     */
    class Bucket<K, V> {
        private K key;
        private V value;
        private int hash;
        private int index;

        public Bucket(K key, V value) {
            this.key = key;
            this.value = value;
            this.hash = Objects.hashCode(key);
            this.index = this.reIndex();
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public int getHash() {
            return hash;
        }

        public int getIndex() {
            return index;
        }

        public int reIndex() {
            this.index = Math.abs(this.hash) % table.length;
            return this.index;
        }
    }
}
