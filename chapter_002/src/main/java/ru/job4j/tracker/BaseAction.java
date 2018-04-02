package ru.job4j.tracker;

/**
 * BaseAction
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public abstract class BaseAction implements UserAction {

    private final int key;
    private final String name;

    /**
     * BaseAction instance constructor
     * @param key int menu action's key
     * @param name String menu action's name
     */
    protected BaseAction(final int key, final String name) {
        this.key = key;
        this.name = name;
    }

    /**
     * Method key
     * @return int menu action's key
     */
    @Override
    public int key() {
        return this.key;
    }

    /**
     * Method info
     * @return String consisting of action's key and name, i.e. description of menu action
     */
    @Override
    public String info() {
        return String.format("%s : %s", this.key, this.name);
    }
}
