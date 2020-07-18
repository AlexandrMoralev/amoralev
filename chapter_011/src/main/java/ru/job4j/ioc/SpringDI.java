package ru.job4j.ioc;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.job4j.tracker.ConsoleInput;

public class SpringDI {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("ru.job4j.ioc");
        context.refresh();
        StartUI ui = context.getBean(StartUI.class);
        ui.add("Petr Arsentev");
        ui.add("Ivan ivanov");
        ui.print();
    }

    @Configuration
    public static class Config {

        @Bean
        public ConsoleInput consoleInput() {
            return new ConsoleInput();
        }
    }

}
