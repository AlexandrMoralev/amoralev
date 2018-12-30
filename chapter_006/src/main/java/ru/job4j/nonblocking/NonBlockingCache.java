package ru.job4j.nonblocking;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.ConcurrentHashMap;

/**
 * NonBlockingCache
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@ThreadSafe
public class NonBlockingCache {
    @GuardedBy("this")
    private final ConcurrentHashMap<Integer, Base> cache;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    /**
     * Constructs instance with default capacity
     */
    public NonBlockingCache() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Constructs NonBlockingCache instance with an initial capacity
     *
     * @param initCapacity int initial capacity
     */
    public NonBlockingCache(final int initCapacity) {
        this.cache = new ConcurrentHashMap<>(initCapacity);
    }

    /**
     * Method add - adds element in this cache, if
     *
     * @param model notnull Base instance to be added
     * @return the previous value associated with the specified model id;
     * null, if there was no such element in the cache
     */
    public synchronized Base add(final Base model) {
        return this.cache.putIfAbsent(model.getId(), model);
    }

    /**
     * Method update - updates current model in the cache,
     * with checking actual version of the updated model
     *
     * @param model notnull Base instance to be updated
     * @return the new Base instance associated with the specified model,
     * null if none
     */
    public synchronized Base update(final Base model) {
        return this.cache.computeIfPresent(model.getId(),
                (id, replacedModel) -> {
                    if (replacedModel.getVersion() != model.getVersion()) {
                        throw new OptimisticException("concurrency in action");
                    }
                    replacedModel.updateVersion();
                    return replacedModel;
                }
        );
    }

    /**
     * Method delete - removes the model from the cache
     *
     * @param model deleted Base instance,
     *              null if there is no such model in the cache
     */
    public synchronized void delete(final Base model) {
        this.cache.remove(model.getId());
    }

    /**
     * Method size
     *
     * @return int number of elements in the cache
     */
    public synchronized int size() {
        return this.cache.size();
    }

    /**
     * Method isEmpty - checks is there any elements in this cache.
     *
     * @return true, if the cache contains at least one element;
     * false - otherwise
     */
    public synchronized boolean isEmpty() {
        return this.cache.isEmpty();
    }

    /**
     * Method containsKey - tests if the specified Base is a key in this table.
     *
     * @param key notnull int number to be checked
     * @return true, if the specified number
     * is a key in this table, as determined by the equals method
     * false - otherwise
     */
    public synchronized boolean containsKey(final int key) {
        return this.cache.containsKey(key);
    }

    /**
     * Method containsValue - tests if the specified Base is a value in this table.
     *
     * @param base notnull Base instance to be checked
     * @return true, if the specified object
     * is a value in this table, as determined by the equals method
     * false - otherwise
     */
    public synchronized boolean containsValue(final Base base) {
        return this.cache.containsValue(base);
    }
}
