package ru.job4j.waitnotify;

import java.util.Objects;

/**
 * ParallelSearch
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class ParallelSearch {

    public static void main(String[] args) {
        final Integer poisonPill = Integer.MIN_VALUE;
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>();
        final Thread consumer = new Thread(
                () -> {
                    while (!Thread.interrupted()) {
                        synchronized (queue) {
                            try {
                                if (queue.isEmpty()) {
                                    queue.wait();
                                }
                                Integer item = queue.poll();
                                if (Objects.equals(item, poisonPill)) {
                                    Thread.currentThread().interrupt();
                                } else {
                                    System.out.println(item);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                Thread.currentThread().interrupt();
                            }
                        }
                    }
                }
        );

        consumer.start();
        new Thread(
                () -> {
                    synchronized (queue) {
                        for (int i = 0; i != 3; i++) {
                            queue.offer(i);
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            queue.notifyAll();
                        }
                        queue.offer(poisonPill);
                    }
                }
        ).start();
    }
}