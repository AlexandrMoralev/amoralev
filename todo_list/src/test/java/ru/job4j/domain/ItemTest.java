package ru.job4j.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class ItemTest {

    private Item firstItem;

    private Item secondItem;

    private Item thirdItem;

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        firstItem = new Item();
        firstItem.setId(1);
        firstItem.setDescription("firstItem description");
        firstItem.setCreated(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());
        firstItem.setDone(false);

        secondItem = new Item();
        secondItem.setId(2);
        secondItem.setDescription("secondItem description");
        secondItem.setCreated(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());
        secondItem.setDone(false);

        thirdItem = new Item();
        thirdItem.setId(3);
        thirdItem.setDescription("thirdItem description");
        thirdItem.setCreated(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());
        thirdItem.setDone(false);
    }

    @Test
    void testOneItemJsonMapping() throws JsonProcessingException {

        String itemAsJsonString = mapper.writeValueAsString(firstItem);

        JsonNode jsonNode = mapper.readTree(itemAsJsonString);
        assertEquals(jsonNode.get("id").asInt(), firstItem.getId());
        assertEquals(jsonNode.get("description").asText(), firstItem.getDescription());
        assertEquals(jsonNode.get("created").asLong(), firstItem.getCreated());
        assertEquals(jsonNode.get("done").asBoolean(), firstItem.isDone());

        Item itemFromJson = mapper.readValue(itemAsJsonString, Item.class);
        assertEquals(itemFromJson.getId(), firstItem.getId());
        assertEquals(itemFromJson.getDescription(), firstItem.getDescription());
        assertEquals(itemFromJson.getCreated(), firstItem.getCreated());
        assertEquals(itemFromJson.isDone(), firstItem.isDone());

        assertEquals(itemFromJson, firstItem);
    }

    @Test
    void testItemsCollectionJsonMapping() throws IOException {

        Collection<Item> items = List.of(firstItem, secondItem);

        StringWriter sw = new StringWriter();
        mapper.writeValue(sw, items);

        String itemsAsJson = sw.toString();

        JsonNode jsonNode = mapper.readTree(itemsAsJson);

        Collection<Item> itemsFromJson = Arrays.asList(mapper.treeToValue(jsonNode, Item[].class));

        assertIterableEquals(items, itemsFromJson);
    }

    @Test
    void testItemsJsonMapping() throws IOException {

        Collection<Item> items = List.of(firstItem, secondItem, thirdItem)
                .stream()
                .sorted(Comparator.comparing(Item::getCreated))
                .collect(Collectors.toList());

        StringWriter sw = new StringWriter();
        mapper.writeValue(sw, items);

        String itemsAsJson = sw.toString();

        JsonNode jsonNode = mapper.readTree(itemsAsJson);

        Collection<Item> itemsFromJson = Arrays.asList(mapper.treeToValue(jsonNode, Item[].class));

        assertIterableEquals(items, itemsFromJson);
    }

}
