package ru.job4j.jdbc;

/**
 * UnaryEx - functional interface, unary operation with a possible exception
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface UnaryEx {
    void action() throws Exception;
}
