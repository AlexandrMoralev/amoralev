package ru.job4j.collections;

import java.util.LinkedList;

/**
 * PriorityQueue
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class PriorityQueue {

    private LinkedList<Task> tasks = new LinkedList<>();

    /**
     * Method put - adds Task to tasks List
     * adding position depends on the priority of the task
     *
     * @param task Task to add
     */
    public void put(Task task) {

        int position = 0;

        for (Task aTask : tasks) {
            if (task.getPriority() <= aTask.getPriority()) {
                break;
            }
            position++;
        }
        tasks.add(position, task);
    }

    /**
     * Method take
     * @return Task from tasks List
     */
    public Task take() {
        return this.tasks.poll();
    }
}
