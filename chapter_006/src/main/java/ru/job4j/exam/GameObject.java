package ru.job4j.exam;

/**
 * GameObject
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public abstract class GameObject {
    private Cell position;

    public GameObject(final Cell position) {
        this.position = position;
    }

    public Cell getPosition() {
        return this.position;
    }

    public void setPosition(final Cell cell) {
        this.position = cell;
    }

    @Override
    public String toString() {
        return GameObject.class.getSimpleName()
                + " {"
                + "position=" + position
                + '}';
    }
}
