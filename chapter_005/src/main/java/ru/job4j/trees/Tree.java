package ru.job4j.trees;

import java.util.*;

/**
 * Tree
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Tree<E extends Comparable<E>> implements SimpleTree<E> {

    private Node<E> root;
    private int modCount;

    /**
     * Constructs an empty Tree only with the root
     *
     * @param value E root value
     */
    public Tree(E value) {
        this.root = new Node<>(value);
        this.modCount = 0;
    }

    /**
     * Method add - inserts element in this tree by parent element
     *
     * @param parent E parent element to add a child element
     * @param child  E child element to be added
     * @return boolean true, when element added successfully,
     * boolean false, if there is such element in the tree,
     * or when if there is no parent element in the tree
     */
    @Override
    public boolean add(E parent, E child) {
        isValid(parent);
        isValid(child);
        boolean result = false;
        Optional<Node<E>> parental = findBy(parent);
        if (!findBy(child).isPresent() && parental.isPresent()) {
            parental.get().add(new Node<>(child));
            result = true;
            modCount++;
        }
        return result;
    }

    /**
     * Method findBy - returns Optional of Node by value
     *
     * @param value E value of the search element
     * @return Optional<Node < E>>
     */
    @Override
    public Optional<Node<E>> findBy(E value) {
        isValid(value);
        Optional<Node<E>> result = Optional.empty();
        Queue<Node<E>> data = new LinkedList<>();
        data.offer(this.root);
        while (!data.isEmpty()) {
            Node<E> el = data.poll();
            if (el.eqValue(value)) {
                result = Optional.of(el);
                break;
            }
            for (Node<E> child : el.leaves()) {
                data.offer(child);
            }
        }
        return result;
    }

    /**
     * Method isBinary - checks, if the tree is binary
     *
     * @return true, if all nodes has less than 2 children nodes
     * false, if this tree is not binary
     */
    public boolean isBinary() {
        boolean result = true;
        Queue<Node<E>> data = new LinkedList<>();
        data.offer(this.root);
        while (!data.isEmpty()) {
            Node<E> el = data.poll();
            if (el.leaves().size() > 2) {
                result = false;
                break;
            }
            for (Node<E> leaf : el.leaves()) {
                data.offer(leaf);
            }
        }
        return result;
    }

    private boolean isValid(E value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }
        return true;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int expectedModCount = modCount;
            private int index = 0;

            @Override
            public boolean hasNext() {
                checkModified();
                return index <= modCount;
            }

            @Override
            public E next() {
                checkModified();
                if (index > modCount) {
                    throw new NoSuchElementException();
                }
                int count = 0;
                Queue<Node<E>> queue = new LinkedList<>();
                Optional<Node<E>> result = Optional.empty();
                queue.offer(root);
                while (!queue.isEmpty()) {
                    Node<E> item = queue.poll();
                    if (index == count) {
                        result = Optional.of(item);
                        index++;
                        break;
                    }
                    for (Node<E> leaf : item.leaves()) {
                        queue.offer(leaf);
                    }
                    count++;
                }
                return result.isPresent() ? result.get().getValue() : null;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            private void checkModified() {
                if (expectedModCount != modCount) {
                    throw new ConcurrentModificationException();
                }
            }
        };
    }
}

