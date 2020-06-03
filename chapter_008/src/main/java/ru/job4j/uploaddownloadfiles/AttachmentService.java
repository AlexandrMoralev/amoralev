package ru.job4j.uploaddownloadfiles;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * AttachmentService interface
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface AttachmentService {

    Optional<String> createAttachment(HttpServletRequest request);

    Optional<String> updateAttachment(HttpServletRequest request);

    Optional<File> getAttachment(String attachmentId);

    void deleteAttachment(String attachmentId);

    default <FileItem> Consumer<FileItem> throwingConsumerWrapper(
            ConsumerEx<FileItem, Exception> consumerEx) {
        return i -> {
            try {
                consumerEx.accept(i);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }
}
