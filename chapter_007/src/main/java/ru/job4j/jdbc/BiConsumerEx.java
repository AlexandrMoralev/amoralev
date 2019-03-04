package ru.job4j.jdbc;

/**
 * BiConsumerEx - functional interface, takes two params with a possible exception
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface BiConsumerEx<L, R> {
    /**
     * Accept two args
     * @param left first arg
     * @param right second arg
     * @throws Exception possible exception
     */
    void accept(L left, R right) throws Exception;
}
