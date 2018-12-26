package ru.job4j.waitnotify;

/**
 * ParallelSearch
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class ParallelSearch {

    public static void main(String[] args) {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>();
        final Thread consumer = new Thread(
                () -> {
                    synchronized (queue) {
                        while (!Thread.interrupted()) {
                            try {
                                if (queue.isEmpty()) {
                                    queue.wait();
                                }
                                System.out.println(queue.poll());
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                        queue.notifyAll();
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
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        consumer.interrupt();
                    }
                }
        ).start();
    }
}