package ru.job4j.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.ui.model.ErrorResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import static ru.job4j.util.Constants.DEFAULT_ENCODING;
import static ru.job4j.util.Constants.MIME_TYPE;

public class JsonUtil {

    private static final Logger LOG = LogManager.getLogger(JsonUtil.class);

    public static <T> Optional<T> extractObject(String propertyName, HttpServletRequest req, Class<T> clazz) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = req.getReader()) {
            String payload;
            while ((payload = br.readLine()) != null) {
                sb.append(payload);
            }
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(sb.toString());
            return Optional.ofNullable(mapper.readValue(jsonNode.get(propertyName).toString(), clazz));
        } catch (IOException e) {
            LOG.error("Error extracting object {}", propertyName, e);
        }
        return Optional.empty();
    }

    public static void sendError(ErrorResponse error, HttpServletResponse response) {
        LOG.info("Sending error response: {}", error);
        response.setStatus(error.getStatus());
        sendResponse(error, response);
    }

    public static void answerWithStatus(int statusCode, HttpServletResponse response) {
        response.setStatus(statusCode);
        sendResponse("{}", response);
    }

    public static <T> void sendResponse(T obj, HttpServletResponse response) {
        try (PrintWriter out = response.getWriter()) {
            ObjectMapper objectMapper = new ObjectMapper();
            response.setContentType(MIME_TYPE);
            response.setCharacterEncoding(DEFAULT_ENCODING);
            out.print(objectMapper.writeValueAsString(obj));
            out.flush();
        } catch (IOException e) {
            LOG.error("Error sending response: {}", obj.toString(), e);
        }
    }

}
