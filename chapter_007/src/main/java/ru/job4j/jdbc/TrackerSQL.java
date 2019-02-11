package ru.job4j.jdbc;

import ru.job4j.tracker.ITracker;
import ru.job4j.tracker.Item;

import java.util.List;

public class TrackerSQL implements ITracker, AutoCloseable {

    @Override
    public Item add(Item item) {
        return null;
    }

    @Override
    public void replace(String id, Item item) {

    }

    @Override
    public void delete(String id) {

    }

    @Override
    public List<Item> findAll() {
        return null;
    }

    @Override
    public List<Item> findByName(String key) {
        return null;
    }

    @Override
    public Item findById(String id) {
        return null;
    }

    @Override
    public void close() throws Exception {

    }
}
