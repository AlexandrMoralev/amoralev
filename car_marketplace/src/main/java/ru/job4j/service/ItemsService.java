package ru.job4j.service;

import ru.job4j.entity.Item;
import ru.job4j.entity.User;
import ru.job4j.ui.dto.FilterInfo;
import ru.job4j.ui.dto.ItemDto;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * ItemsService
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface ItemsService {

    Item saveItem(ItemDto itemDto, User user);

    Optional<Item> getItem(Integer itemId);

    void updateItem(ItemDto itemDto);

    void deleteItem(Integer itemId);

    Stream<Item> getAllItems();

    Stream<Item> getActiveItems();

    Stream<Item> getActiveItems(FilterInfo filter);

    Stream<Item> getUserItems(Integer userId);

    Stream<Item> getAllItems(FilterInfo filter);

}
