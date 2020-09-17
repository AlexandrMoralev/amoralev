package ru.job4j.ui.model;

import ru.job4j.domain.Item;

import java.util.List;
import java.util.stream.Collectors;

public class ItemsDTO {

    private final String author;

    private final List<ItemDTO> tasks;

    public ItemsDTO(String author,
                    List<Item> tasks
    ) {
        this.author = author;
        this.tasks = tasks.stream().map(ItemDTO::fromModel).collect(Collectors.toList());
    }

    public String getAuthor() {
        return author;
    }

    public List<ItemDTO> getTasks() {
        return tasks;
    }
}
