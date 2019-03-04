package ru.job4j.jdbc;

/**
 * ConsumerEx - functional interface, takes a single param with a possible exception
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface ConsumerEx<T> {
    /**
     * Accept arg
     * @param param arg
     * @throws Exception possible exception
     */
    void accept(T param) throws Exception;
}
