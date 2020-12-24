package ru.job4j.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * RunnbleEx - functional interface, runnable with a possible exception
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@FunctionalInterface
public interface RunnableEx extends Runnable {

    Logger LOG = LogManager.getLogger(RunnableEx.class);

    @Override
    default void run() {
        try {
            tryRun();
        } catch (final Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    void tryRun() throws Exception;
}
