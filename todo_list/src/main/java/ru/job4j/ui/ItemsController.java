package ru.job4j.ui;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.domain.Item;
import ru.job4j.persistence.ItemsDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ItemsController", urlPatterns = "/items")
public class ItemsController extends HttpServlet {

    private final static Logger LOG = LogManager.getLogger(ItemsController.class);
    private final ItemsDao itemsDao = ItemsDao.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try (PrintWriter writer = resp.getWriter()) {
            ObjectMapper mapper = new ObjectMapper();
            writer.println(mapper.writeValueAsString(itemsDao.getItems()));
            writer.flush();
        } catch (Exception e) {
            LOG.error("Error getting items", e);
            resp.sendError(500);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Item item = extractObject(req, "item", Item.class);
        itemsDao.addItem(item);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Item item = extractObject(req, "item", Item.class);
        itemsDao.updateItem(item.getId());
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer itemToDeleteId = extractObject(req, "id", Integer.class);
        itemsDao.deleteItem(itemToDeleteId);
    }

    private <T> T extractObject(HttpServletRequest req, String propertyName, Class<T> clazz) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = req.getReader();
        String payload;
        while ((payload = br.readLine()) != null) {
            sb.append(payload);
        }
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(sb.toString());
        return mapper.readValue(jsonNode.get(propertyName).toString(), clazz);
    }
}
