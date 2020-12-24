package ru.job4j.service;

import ru.job4j.entity.Item;
import ru.job4j.entity.User;
import ru.job4j.ui.dto.FilterInfo;
import ru.job4j.ui.dto.ItemDto;

import java.util.Collection;
import java.util.Optional;

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

    Collection<Item> getAllItems();

    Collection<Item> getActiveItems();

    Collection<Item> getActiveItems(FilterInfo filter);

    Collection<Item> getUserItems(Integer userId);

    Collection<Item> getAllItems(FilterInfo filter);

}
