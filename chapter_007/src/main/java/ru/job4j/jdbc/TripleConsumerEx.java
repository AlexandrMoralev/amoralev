package ru.job4j.jdbc;

/**
 * TripleConsumerEx - interface, takes three params with a possible exception
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface TripleConsumerEx<T, R, E> {
    /**
     * Accept three args
     * @param first first arg
     * @param second second arg
     * @param third second arg
     * @throws Exception possible exception
     */
    void accept(T first, R second, E third) throws Exception;
}
