package ru.job4j.ui.cache;

/**
 * Cache
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface Cache {

    void add(String key, Object value, long expirationPeriod);

    void remove(String key);

    Object get(String key);

    void clear();

    long size();

    boolean contains(Integer userId);
}
