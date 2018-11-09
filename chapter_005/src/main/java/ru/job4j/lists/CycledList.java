package ru.job4j.lists;

/**
 * CycledList
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class CycledList<T> {

    public class Node<T> {
        T value;
        Node<T> next;

        public Node(T value) {
            this.value = value;
        }
    }

    /**
     * Method hasCycle - finds reference loop in this list
     *
     * @param first T non-null element of the list
     * @return boolean true, if there is a loop in this list,
     *                 false, if the list hasn't reference loop
     */
    public boolean hasCycle(Node<T> first) {
        if (first == null) {
            throw new IllegalArgumentException();
        }
        boolean result = false;
        Node<T> slow = first;
        Node<T> fast = first;

        while (!result) {
            if (slow.next == null || fast.next.next == null) {
                break;
            }
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                result = true;
            }
        }
        return result;
    }
}
