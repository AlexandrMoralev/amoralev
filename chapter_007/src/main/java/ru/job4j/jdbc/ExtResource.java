package ru.job4j.jdbc;

/**
 * ExtResource - read or write a resource to an external system with a possible exception
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface ExtResource<T> {

    /**
     * Read resource by name
     *
     * @param name of the resource
     * @return value
     * @throws Exception possible exception
     */
    T read(String name) throws Exception;

    /**
     * Write resource
     *
     * @param value resource
     * @throws Exception possible exception
     */
    void write(T value) throws Exception;
}
