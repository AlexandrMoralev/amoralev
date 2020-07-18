package ru.job4j.ioc;

import org.springframework.stereotype.Component;
import ru.job4j.tracker.ConsoleInput;

@Component
public class StartUI {

    private Store store;
    private ConsoleInput consoleInput;

    public StartUI(Store store,
                   ConsoleInput consoleInput
    ) {
        this.store = store;
        this.consoleInput = consoleInput;
    }

    public void add(String value) {
        store.add(value);
    }

    public void print() {
        store.getAll().forEach(System.out::println);
    }

}
