package ru.job4j.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Function;

/**
 * FunctionEx - functional interface with a possible exception
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@FunctionalInterface
public interface FunctionEx<T, R, E extends Exception> extends Function<T, R> {

    Logger LOG = LogManager.getLogger(FunctionEx.class);

    R tryApply(T arg) throws E;

    @Override
    default R apply(T t) {
        try {
            return tryApply(t);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    static <T, R, E extends Exception> Function<T, R> wrapper(FunctionEx<T, R, E> functionEx) {
        return functionEx;
    }
}
