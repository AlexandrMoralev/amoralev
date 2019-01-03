package ru.job4j.pools;

import ru.job4j.waitnotify.SimpleBlockingQueue;

import java.util.LinkedList;
import java.util.List;

/**
 * ThreadPool
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class ThreadPool {

    private final int size;
    private final List<Thread> threads;
    private final SimpleBlockingQueue<Runnable> tasks;

    /**
     * Constructs ThreadPool instance
     */
    public ThreadPool() {
        this.size = Runtime.getRuntime().availableProcessors();
        this.threads = new LinkedList<>();
        this.tasks = new SimpleBlockingQueue<>();
        initThreads();
    }

    private void initThreads() {
        for (int i = 0; i < size; i++) {
            this.threads.add(new Worker(this.tasks));
        }
        this.threads.forEach(Thread::start);
    }

    /**
     * Method work - adds a task to the execution queue
     *
     * @param job Runnable task
     */
    public void work(final Runnable job) {
        this.tasks.offer(job);
    }

    /**
     * Method shutdown - interrupts all threads in this pool
     */
    public void shutdown() {
        threads.forEach(Thread::interrupt);
    }

    private class Worker extends Thread {

        private final SimpleBlockingQueue<Runnable> tasks;

        private Worker(final SimpleBlockingQueue<Runnable> tasks) {
            this.tasks = tasks;
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    tasks.poll().run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
