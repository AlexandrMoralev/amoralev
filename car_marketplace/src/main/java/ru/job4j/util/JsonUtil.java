package ru.job4j.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.ui.dto.ErrorResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static ru.job4j.Constants.DEFAULT_ENCODING;
import static ru.job4j.Constants.MIME_TYPE;
import static ru.job4j.exception.FunctionEx.wrapper;

/**
 * JsonUtil
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class JsonUtil {

    private static final Logger LOG = LogManager.getLogger(JsonUtil.class);
    private static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();
        MAPPER.registerModule(new Jdk8Module());
    }

    public static <T> Optional<T> extractObject(String propertyName, HttpServletRequest req, Class<T> clazz) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = req.getReader()) {
            String payload;
            while ((payload = br.readLine()) != null) {
                sb.append(payload);
            }
            JsonNode jsonNode = MAPPER.readTree(sb.toString());
            return ofNullable(jsonNode.get(propertyName))
                    .map(JsonNode::toString)
                    .filter(v -> !v.strip().isBlank())
                    .map(wrapper(v -> MAPPER.readValue(v, clazz)));
        } catch (Exception e) {
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
            response.setContentType(MIME_TYPE);
            response.setCharacterEncoding(DEFAULT_ENCODING);
            out.print(MAPPER.writeValueAsString(obj));
            out.flush();
        } catch (IOException e) {
            LOG.error("Error sending response", e);
        }
    }

}
