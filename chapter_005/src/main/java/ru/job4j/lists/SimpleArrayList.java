package ru.job4j.lists;

/**
 * SimpleArrayList
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class SimpleArrayList<E> {

    private int size;
    private Node<E> first;

    /**
     * Method add - inserts data as first element
     *
     * @param data element to be added
     */
    public void add(E data) {
        Node<E> newLink = new Node<>(data);
        newLink.next = this.first;
        this.first = newLink;
        this.size++;
    }

    /**
     * Method delete - removes first element
     *
     * @return deleted element
     */
    public E delete() {
        Node<E> result = this.first;
        this.first = first.next;
        this.size--;
        return result.data;
    }

    /**
     * Method get - returns element by index
     *
     * @param index int
     * @return E element
     */
    public E get(int index) {
        Node<E> result = this.first;
        for (int i = 0; i < index; i++) {
            result = result.next;
        }
        return result.data;
    }

    /**
     * Method size
     *
     * @return int number of non-null elements
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Class Node - data storage
     *
     * @param <E>
     */
    private static class Node<E> {

        E data;
        Node<E> next;

        Node(E data) {
            this.data = data;
        }
    }
}

