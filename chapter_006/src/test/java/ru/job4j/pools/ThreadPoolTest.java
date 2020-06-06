package ru.job4j.pools;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * ThreadPoolTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class ThreadPoolTest {

    private ThreadPool pool;
    private AtomicInteger sum;

    @BeforeEach
    public void init() {
        pool = new ThreadPool();
    }

    @Test
    public void whenPoolCountingTo1MShouldReturn1M() {
        sum = new AtomicInteger(0);
        for (int i = 0; i < 1_000_000; i++) {
            pool.work(() -> sum.incrementAndGet());
        }
        pool.shutdown();
        assertThat(sum.get(), is(1_000_000));
    }

    @Test
    public void whenPoolDecrementingFrom1MToZeroShouldReturnZero() {
        sum = new AtomicInteger(1_000_000);
        for (int i = 0; i < 1_000_000; i++) {
            pool.work(() -> sum.decrementAndGet());
        }
        pool.shutdown();
        assertThat(sum.get(), is(0));
    }

    @Test
    public void whenPoolCalculatesShouldReturnCorrectAnswer() {
        final long[] numbers = {16, 17, 18, 19, 20};
        Runnable[] tasks = new Runnable[5];
        for (int i = 0; i < tasks.length; i++) {
            final int index = i;
            tasks[i] = () -> {
                numbers[index] = factorial(numbers[index]);
            };
            pool.work(tasks[i]);
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pool.shutdown();
        assertThat(numbers[0] == 20922789888000L
                        && numbers[1] == 355687428096000L
                        && numbers[2] == 6402373705728000L
                        && numbers[3] == 121645100408832000L
                        && numbers[4] == 2432902008176640000L,
                is(true)
        );
    }

    private long factorial(long num) {
        if (num < 0 || num > 20) {
            throw new IllegalArgumentException();
        }
        if (num <= 1) {
            return 1;
        }
        return num * factorial(num - 1);
    }
}
