package ru.job4j.nonblocking;

/**
 * OptimisticException
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class OptimisticException extends RuntimeException {

    public OptimisticException() {
        super();
    }

    public OptimisticException(String message) {
        super(message);
    }

    public OptimisticException(String message, Throwable cause) {
        super(message, cause);
    }

    public OptimisticException(Throwable cause) {
        super(cause);
    }

    protected OptimisticException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
