package ru.job4j.ioc;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Store {

    private List<String> data = new ArrayList<>();

    public void add(String value) {
        this.data.add(value);
    }

    public List<String> getAll() {
        return this.data;
    }

}
