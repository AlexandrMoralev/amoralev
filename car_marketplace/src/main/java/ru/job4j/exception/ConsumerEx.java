package ru.job4j.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

/**
 * ConsumerEx - functional interface, consumer with a possible exception
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@FunctionalInterface
public interface ConsumerEx<T> extends Consumer<T> {

    Logger LOG = LogManager.getLogger(RunnableEx.class);

    void tryAccept(T t) throws Exception;

    @Override
    default void accept(T t) {
        try {
            tryAccept(t);
        } catch (final Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

}
