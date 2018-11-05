package ru.job4j.generics;

/**
 * AbstractStore
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class AbstractStore<T extends Base> implements Store<T> {

    private SimpleArray<T> storage;

    AbstractStore(int size) {
        this.storage = new SimpleArray<>(size);
    }

    @Override
    public void add(final T model) {
        this.storage.add(model);
    }

    @Override
    public boolean replace(final String id, final T model) {
        int index = getIndex(id);
        return index != -1 && this.storage.set(index, model);
    }

    @Override
    public boolean delete(final String id) {
        int index = getIndex(id);
        return index != -1 && this.storage.delete(index);
    }

    @Override
    public T findById(final String id) {
        int index = getIndex(id);
        return index == -1 ? null : this.storage.get(index);
    }

    private int getIndex(final String id) {
        int index = -1;
        int size = this.storage.size();
        for (int i = 0; i < size; i++) {
            if (id.equals(this.storage.get(i).getId())) {
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (T value: this.storage) {
            sb.append(value.getId()).append(" ");
        }
        return sb.toString();
    }
}
