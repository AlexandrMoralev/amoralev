package ru.job4j.jdbc;

public class Usage<T> {

    private void ex(UnaryEx unary) {
        try {
            unary.action();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads resource without exception.
     *
     * @param name     name.
     * @param resource resource.
     */
    public void read(String name, ExtResource<T> resource) {
        ex(() -> resource.read((name)));
    }

    /**
     * Writes resource without exception.
     *
     * @param value    value.
     * @param resource resources.
     */
    public void write(T value, ExtResource<T> resource) {
        ex(() -> resource.write(value));
    }
}
