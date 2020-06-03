package ru.job4j.uploaddownloadfiles;

@FunctionalInterface
public interface ConsumerEx<T, E extends Exception> {
    void accept(T t) throws E;
}
