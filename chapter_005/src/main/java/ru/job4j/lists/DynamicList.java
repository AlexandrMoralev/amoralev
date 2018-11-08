package ru.job4j.lists;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * DynamicList
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class DynamicList<E> implements Iterable<E> {

    private Node<E> first;
    private Node<E> last;
    private int counter;
    private int modified;

    /**
     * DynamicList instance constructor, constructs an empty list.
     */
    public DynamicList() {
        this.first = null;
        this.last = null;
        this.counter = 0;
        this.modified = 0;
        LinkedList l = new LinkedList();
    }

    /**
     * Method add - adds e as last element of this list
     *
     * @param value E
     */
    public void add(E value) {
        Node<E> newNode;
        if (this.first == null) {
            newNode = new Node<>(value, null, null);
            this.first = newNode;
            this.last = newNode;
        } else {
            newNode = new Node<>(value, this.last, null);
            this.last.next = newNode;
            this.last = newNode;
        }
        counter++;
        modified++;
    }

    /**
     * Method get - returns element by index
     *
     * @param index index of the element in the list
     * @return E element
     */
    public E get(int index) {
        if (index < 0 || index > counter) {
            throw new NoSuchElementException("No such element");
        }
        Node<E> result = first;
        for (int i = 0; i != index; i++) {
            result = result.next;
        }
        return result.data;
    }

    public int size() {
        return counter;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int iteration = 0;
            int expectedModCount = modified;
            Node<E> current = first;

            @Override
            public boolean hasNext() {
                isModified();
                return current != null;
            }

            @Override
            public E next() {
                isModified();
                if (!hasNext()) {
                    throw new NoSuchElementException("No such element");
                }
                E result = current.data;
                current = current.next;
                iteration++;
                return result;
            }

            private void isModified() {
                if (expectedModCount != modified) {
                    throw new ConcurrentModificationException("ConcurrentModification state");
                }
            }
        };
    }

    private class Node<E> {

        private Node<E> prev;
        private Node<E> next;
        private E data;

        Node(E data, Node<E> prev, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }
}
