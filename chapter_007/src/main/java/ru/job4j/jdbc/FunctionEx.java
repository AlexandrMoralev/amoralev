package ru.job4j.jdbc;

/**
 * FunctionEx - functional interface, takes two params with a possible exception
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface FunctionEx<T, R> {

    R apply(T arg);
}
