package ru.job4j.waitnotify;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * SimpleBlockingQueue
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class SimpleBlockingQueueTest {

    private SimpleBlockingQueue<Integer> queue;
    private Thread producer;
    private Thread consumer;
    private final Integer[] array = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private Integer[] result;

    @Before
    public void init() {
        queue = new SimpleBlockingQueue<>();
        result = new Integer[array.length];
        producer = new Thread(() -> {
            for (Integer num : array) {
                queue.offer(num);
            }
        });
        consumer = new Thread(() -> {
            for (int i = 0; i < result.length; i++) {
                result[i] = queue.poll();
            }
        });
    }


    @Test
    public void whenQueueIsEmptyThenQueueSizeIsZero() {
        assertThat(queue.isEmpty(), is(true));
        queue.offer(0);
        assertThat(queue.isEmpty(), is(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenOfferNullShouldThrowIAException() {
        queue.offer(null);
    }

    @Test
    public void whenProducingItemsShouldConsumeWithSameOrder() {
        producer.start();
        consumer.start();
        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertThat(result, is(array));
    }

}
