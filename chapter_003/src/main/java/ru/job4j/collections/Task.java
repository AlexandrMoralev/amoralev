package ru.job4j.collections;

/**
 * Task
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Task {
    private String desc;
    private int priority;

    /**
     * Task instance constructor
     * @param desc String description of the Task
     * @param priority int priority value, 0 is the highest
     */
    public Task(String desc, int priority) {
        this.desc = desc;
        this.priority = priority;
    }

    /**
     * getDesc
     * @return String description of the Task
     */
    public String getDesc() {
        return desc;
    }

    /**
     * getPriority
     * @return int priority value, 0 is the highest
     */
    public int getPriority() {
        return priority;
    }
}
