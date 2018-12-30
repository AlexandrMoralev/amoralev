package ru.job4j.nonblocking;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * NonBlockingCache
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class NonBlockingCacheTest {
    private NonBlockingCache cache;
    private Thread threadA;
    private Thread threadB;
    private Base first;
    private Base second;
    private Base third;

    @Before
    public void init() {
        cache = new NonBlockingCache();
        first = new Base(1);
        second = new Base(2);
        third = new Base(3);
    }

    //adding tests
    @Test
    public void whenAddItemsThenCacheContainsItems() throws InterruptedException {
        threadA = new Thread(() -> {
            cache.add(first);
            cache.add(second);
            cache.add(third);
        });
        threadB = new Thread(() -> {
            cache.add(first);
            cache.add(second);
            cache.add(third);
        });
        threadA.start();
        threadB.start();
        threadA.join();
        threadB.join();
        assertThat(cache.size(), is(3));
        assertThat(cache.containsValue(first), is(true));
        assertThat(cache.containsValue(second), is(true));
        assertThat(cache.containsValue(third), is(true));
    }

    // updating tests
    @Test
    public void whenUpdateItemsThenCacheContainsUpdatedItems() throws InterruptedException {
        cache.add(first);
        cache.add(second);
        cache.add(third);
        threadA = new Thread(() -> {
            cache.update(first);
            cache.update(second);
            cache.update(third);
        });
        threadB = new Thread(() -> {
            cache.update(first);
            cache.update(second);
        });
        new Thread(() -> cache.update(first)).start();
        threadA.start();
        threadA.join();
        threadB.start();
        threadB.join();
        assertThat(cache.size(), is(3));
        assertThat(cache.containsValue(first), is(true));
        assertThat(first.getVersion(), is(3));
        assertThat(cache.containsValue(second), is(true));
        assertThat(second.getVersion(), is(2));
        assertThat(cache.containsValue(third), is(true));
        assertThat(third.getVersion(), is(1));
    }

    //deleting tests
    @Test
    public void whenDeleteItemsThenCacheNotContainsItems() throws InterruptedException {
        threadA = new Thread(() -> {
            cache.add(first);
            cache.add(second);
            cache.add(third);
        });
        threadB = new Thread(() -> {
            cache.delete(first);
            cache.delete(second);
            cache.delete(third);
        });
        threadA.start();
        threadB.start();
        threadA.join();
        threadB.join();
        assertThat(cache.size(), is(0));
        assertThat(cache.containsValue(first), is(false));
        assertThat(cache.containsValue(second), is(false));
        assertThat(cache.containsValue(third), is(false));
    }

    // exceptions test
    @Test
    public void whenThrowException() throws InterruptedException {
        final AtomicReference<Exception> exA = new AtomicReference<>();
        final AtomicReference<Exception> exB = new AtomicReference<>();
        cache.add(first);
        threadA = new Thread(() -> {
            try {
                cache.update(first);
                cache.update(new Base(1));
                cache.update(new Base(1));
            } catch (RuntimeException e) {
                exA.set(e);
            }

        });
        threadB = new Thread(() -> {
            try {
                cache.update(first);
                cache.update(first);
                cache.update(new Base(1));
                cache.update(new Base(1));
            } catch (RuntimeException e) {
                exB.set(e);
            }
        });
        threadA.start();
        threadB.start();
        threadA.join();
        threadB.join();

        assertThat(exA.get().getMessage(), is("concurrency in action"));
        assertThat(exB.get().getMessage(), is("concurrency in action"));
    }
}
