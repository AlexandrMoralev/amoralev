package ru.job4j.trees;

import java.util.Optional;

/**
 * SimpleTree
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface SimpleTree<E extends Comparable<E>> extends Iterable<E> {
    /**
     * Method add
     *
     * @param parent parent.
     * @param child  child.
     * @return true when added successfully, false if the element is not added
     */
    boolean add(E parent, E child);

    Optional<Node<E>> findBy(E value);
}
