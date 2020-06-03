package ru.job4j.uploaddownloadfiles;

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;

import static java.util.function.Predicate.not;

/**
 * PhotoService
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@ThreadSafe
public enum PhotoService implements AttachmentService {
    INSTANCE;

    private static final Logger LOG = LogManager.getLogger(PhotoService.class);
    private final DiskFileItemFactory fileItemFactory;

    PhotoService() {
        this.fileItemFactory = new DiskFileItemFactory();
    }

    @Override
    public Optional<String> createAttachment(HttpServletRequest request) {
        return processRequestWith(createFile, request);
    }


    @Override
    public Optional<String> updateAttachment(HttpServletRequest request) {
        return processRequestWith(updateFile, request);
    }

    @Override
    public Optional<File> getAttachment(String path) {
        return Files.exists(Paths.get(path)) ? Optional.of(new File(path)) : Optional.empty();
    }

    @Override
    public void deleteAttachment(String path) {
        Path deletableFilePath = Paths.get(path);
        if (Files.exists(deletableFilePath)) {
            try {
                Files.delete(deletableFilePath);
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    private Optional<String> processRequestWith(BiFunction<File, Collection<FileItem>, Optional<String>> function,
                                                HttpServletRequest request
    ) {
        ServletContext servletContext = request.getServletContext();
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        fileItemFactory.setRepository(repository);
        ServletFileUpload upload = new ServletFileUpload(fileItemFactory);
        try {
            File folder = new File(servletContext.getRealPath("/images/"));
            if (!folder.exists()) {
                folder.mkdir();
            }
            List<FileItem> items = upload.parseRequest(request);
            return function.apply(folder, items);
        } catch (FileUploadException e) {
            LOG.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    private BiFunction<File, Collection<FileItem>, Optional<String>> createFile = (folder, items) -> {
        AtomicReference<String> photoId = new AtomicReference<>();
        items.stream()
                .filter(not(FileItem::isFormField))
                .findFirst()
                .ifPresent(
                        throwingConsumerWrapper(
                                create(folder, photoId)
                        )
                );
        return Optional.of(photoId.get());
    };

    private ConsumerEx<FileItem, Exception> create(File folder, AtomicReference<String> photoId) {
        return item -> {
            String name = item.getName();
            File file = new File(folder + File.separator + name);
            try (FileOutputStream out = new FileOutputStream(file)) {
                out.write(item.getInputStream().readAllBytes());
                photoId.set(name);
            }
        };
    }

    private BiFunction<File, Collection<FileItem>, Optional<String>> updateFile = (folder, items) -> {
        AtomicReference<String> photoId = new AtomicReference<>();
        items.stream()
                .filter(not(FileItem::isFormField))
                .findFirst()
                .ifPresent(
                        throwingConsumerWrapper(
                                update(folder, photoId)
                        )
                );
        return Optional.of(photoId.get());
    };

    private ConsumerEx<FileItem, Exception> update(File folder, AtomicReference<String> photoId) {
        return item -> {
            String name = item.getName();
            File file = new File(folder + File.separator + name);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            try (FileOutputStream out = new FileOutputStream(file)) {
                out.write(item.getInputStream().readAllBytes());
                photoId.set(name);
            }
        };
    }


}
