package ru.job4j.ui.cache;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.Config;

import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;
import static java.util.function.Predicate.not;

public class InMemoryCache implements Cache {

    private static final Logger LOG = LogManager.getLogger(InMemoryCache.class);

    private final ConcurrentHashMap<String, SoftReference<CachedObject>> cache;

    public InMemoryCache(Config config) {
        int cleanUpPeriod = config.getInt("cache.cleanup.period.seconds") * 1000;
        int initCapacity = config.getInt("cache.init.capacity");
        float loadFactor = config.getFloat("cache.load.factor");
        int concurrencyLevel = config.getInt("cache.concurrency.level");

        this.cache = new ConcurrentHashMap<>(initCapacity, loadFactor, concurrencyLevel);

        Thread cacheCleaner = new Thread(
                () -> {
                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            Thread.sleep(cleanUpPeriod);
                            LOG.debug("Starting cache cleanup");
                            cache.entrySet()
                                    .removeIf(entry -> ofNullable(entry.getValue())
                                            .map(SoftReference::get)
                                            .map(CachedObject::isExpired)
                                            .orElse(false));
                            LOG.debug("Cache cleaned up");
                        } catch (InterruptedException e) {
                            LOG.error("Error while cleaning cache", e);
                            Thread.currentThread().interrupt();
                        }
                    }
                });
        cacheCleaner.setDaemon(true);
        cacheCleaner.start();
    }

    @Override
    public void add(String key, Object value, long expirationPeriodMillis) {
        if (key == null) {
            return;
        }
        if (value == null) {
            cache.remove(key);
        } else {
            long expirationTime = System.currentTimeMillis() + expirationPeriodMillis;
            cache.put(key, new SoftReference<>(new CachedObject(value, expirationTime)));
        }
    }

    @Override
    public void remove(String key) {
        cache.remove(key);
    }

    @Override
    public Object get(String key) {
        return ofNullable(cache.get(key))
                .map(SoftReference::get)
                .filter(cachedObject -> !cachedObject.isExpired())
                .map(CachedObject::getValue)
                .orElse(null);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public long size() {
        return cache.entrySet().stream()
                .filter(entry -> ofNullable(entry.getValue())
                        .map(SoftReference::get)
                        .map(cachedObject -> !cachedObject.isExpired())
                        .orElse(false))
                .count();
    }

    @Override
    public boolean contains(Integer userId) {
        return cache.values().stream()
                .flatMap(
                        cachedObject -> ofNullable(cachedObject.get())
                                .filter(not(CachedObject::isExpired))
                                .filter(obj -> obj.getValue() instanceof Integer)
                                .map(obj -> (Integer) obj.getValue())
                                .stream()
                )
                .anyMatch(userId::equals);
    }

    private static class CachedObject {

        private final Object value;

        private final long expirationTime;

        CachedObject(Object value, long expirationTime) {
            this.value = value;
            this.expirationTime = expirationTime;
        }

        Object getValue() {
            return value;
        }

        long getExpirationTime() {
            return expirationTime;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > this.expirationTime;
        }
    }

}
