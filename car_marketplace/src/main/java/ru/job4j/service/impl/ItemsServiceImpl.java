package ru.job4j.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.entity.*;
import ru.job4j.persistence.impl.*;
import ru.job4j.service.ItemsService;
import ru.job4j.ui.dto.FilterInfo;
import ru.job4j.ui.dto.ItemDto;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * ItemsServiceImpl
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class ItemsServiceImpl implements ItemsService {

    private static final Logger LOG = LogManager.getLogger(ItemsServiceImpl.class);
    private static final Comparator<Item> DATE_TIME_COMPARATOR = Comparator.comparing(Item::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder()));

    private final ItemsDao itemsDao;
    private final UsersDao usersDao;
    private final CarDao carDao;
    private final ProductionInfoDao productionInfoDao;
    private final TransmissionDao transmissionDao;
    private final EngineDao engineDao;

    public ItemsServiceImpl(ItemsDao itemsDao,
                            UsersDao usersDao,
                            CarDao carDao,
                            ProductionInfoDao productionInfoDao,
                            TransmissionDao transmissionDao,
                            EngineDao engineDao
    ) {
        this.itemsDao = itemsDao;
        this.usersDao = usersDao;
        this.carDao = carDao;
        this.productionInfoDao = productionInfoDao;
        this.transmissionDao = transmissionDao;
        this.engineDao = engineDao;
    }

    @Override
    public Item saveItem(ItemDto itemDto, User user) {

        Engine engine = itemDto.getCar().getProductionInfo().getEngine().toEntity();
        this.engineDao.save(engine);

        Transmission transmission = itemDto.getCar().getProductionInfo().getTransmission().toEntity();
        this.transmissionDao.save(transmission);

        ProductionInfo productionInfo = itemDto.getCar().getProductionInfo().toEntity(engine, transmission);
        this.productionInfoDao.save(productionInfo);

        Car car = itemDto.getCar().toEntity(productionInfo);
        this.carDao.save(car);

        Item item = itemDto.toEntity(user, car);
        this.itemsDao.save(item);

        user.addItem(item);
        this.usersDao.update(user);

        return item;
    }

    @Override
    public Optional<Item> getItem(Integer itemId) {
        return this.itemsDao.find(itemId);
    }

    @Override
    public void updateItem(ItemDto itemDto) {
        this.itemsDao.find(itemDto.getId()).map(item -> {
            item.setActive(itemDto.getActive());
            return item;
        }).ifPresent(this.itemsDao::update);
    }

    @Override
    public void deleteItem(Integer itemId) {
        this.itemsDao.deleteById(itemId);
    }

    @Override
    public Stream<Item> getAllItems() {
        return this.itemsDao.findAll();
    }

    @Override
    public Stream<Item> getAllItems(FilterInfo filter) {
        return this.itemsDao.findAll(filter);
    }

    @Override
    public Stream<Item> getActiveItems() {
        return this.itemsDao.findActive();
    }

    @Override
    public Stream<Item> getActiveItems(FilterInfo filter) {
        return this.itemsDao.findActive(filter);
    }

    @Override
    public Stream<Item> getUserItems(Integer userId) {
        return this.itemsDao.findByUser(userId);
    }
}
