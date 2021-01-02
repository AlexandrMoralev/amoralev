package ru.job4j.testing.base;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrdersStoreTest {

    private BasicDataSource pool = new BasicDataSource();

    @BeforeEach
    public void setUp() throws SQLException {
        pool.setDriverClassName("org.hsqldb.jdbcDriver");
        pool.setUrl("jdbc:hsqldb:mem:tests;sql.syntax_pgs=true");
        pool.setUsername("sa");
        pool.setPassword("");
        pool.setMaxTotal(2);

        StringBuilder builder = new StringBuilder();

        try (FileInputStream fileInputStream = new FileInputStream("./db/update_001.sql");
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
             BufferedReader br = new BufferedReader(inputStreamReader)
        ) {
            br.lines().forEach(line -> builder.append(line).append(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool.getConnection().prepareStatement(builder.toString()).executeUpdate();
    }

    @AfterEach
    public void cleanUp() throws SQLException {
        pool.getConnection().prepareStatement("DROP TABLE orders CASCADE;").executeUpdate();
    }


    @Test
    public void whenSaveOrderAndFindSaved() {
        OrdersStore store = new OrdersStore(pool);

        store.save(Order.of("name1", "description1"));
        store.save(Order.of("name2", "description2"));

        List<Order> all = (List<Order>) store.findAll();

        assertEquals(2, all.size());
        assertEquals(1, all.get(0).getId());
        assertEquals("name1", all.get(0).getName());
        assertEquals("description1", all.get(0).getDescription());

        Order foundById = store.findById(1).orElseThrow(RuntimeException::new);
        assertEquals("name1", foundById.getName());
        assertEquals("description1", foundById.getDescription());

        assertTrue(store.findByName("name").isEmpty());

        Order foundByName = store.findByName("name2").orElseThrow(RuntimeException::new);
        assertEquals(2, foundByName.getId());
        assertEquals("description2", foundByName.getDescription());
    }

    @Test
    public void whenUpdateOrderAndFindUpdated() {
        OrdersStore store = new OrdersStore(pool);

        Order order1 = store.save(Order.of("name1", "description1"));
        Order order2 = store.save(Order.of("name2", "description2"));

        List<Order> all = (List<Order>) store.findAll();

        assertEquals(2, all.size());

        all.stream()
                .map(o -> new Order(
                        o.getId(),
                        "new_" + o.getName(),
                        "new_" + o.getDescription(),
                        o.getCreated()
                )).forEach(store::update);

        all = (List<Order>) store.findAll();
        assertEquals(2, all.size());
        assertTrue(all.stream().allMatch(o -> o.getName().startsWith("new_")));
        assertTrue(all.stream().allMatch(o -> o.getDescription().startsWith("new_")));

        Order foundById1 = store.findById(order1.getId()).orElseThrow(RuntimeException::new);
        assertEquals("new_name1", foundById1.getName());
        assertEquals("new_description1", foundById1.getDescription());

        Order foundById2 = store.findById(order2.getId()).orElseThrow(RuntimeException::new);
        assertEquals("new_name2", foundById2.getName());
        assertEquals("new_description2", foundById2.getDescription());

        assertTrue(store.findByName("name").isEmpty());
        assertTrue(store.findByName("name1").isEmpty());
        assertTrue(store.findByName("name2").isEmpty());

        Order foundByName1 = store.findByName("new_name1").orElseThrow(RuntimeException::new);
        assertEquals(order1.getId(), foundByName1.getId());
        assertEquals("new_description1", foundByName1.getDescription());

        Order foundByName2 = store.findByName("new_name2").orElseThrow(RuntimeException::new);
        assertEquals(order2.getId(), foundByName2.getId());
        assertEquals("new_description2", foundByName2.getDescription());
    }
}
