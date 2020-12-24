package ru.job4j.ui.controller;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.AppContext;
import ru.job4j.entity.Photo;
import ru.job4j.service.PhotoService;
import ru.job4j.ui.dto.ErrorResponse;
import ru.job4j.ui.dto.PhotoDto;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static java.util.function.Predicate.not;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static org.apache.commons.fileupload.FileUploadBase.CONTENT_DISPOSITION;
import static ru.job4j.util.JsonUtil.sendError;
import static ru.job4j.util.JsonUtil.sendResponse;

/**
 * FilesController
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@WebServlet(name = "FilesController", urlPatterns = "/file")
public class FilesController extends HttpServlet {

    private static final Logger LOG = LogManager.getLogger(FilesController.class);
    private final PhotoService photoService = AppContext.INSTANCE.photoService;

    private static final int MB = 1024 * 1024;
    private static final int MEMORY_THRESHOLD = 4 * MB; // TODO read from properties
    private static final long MAX_FILE_SIZE = 5 * MB;
    private static final long MAX_REQUEST_SIZE = 8 * MB;
    private static final String DEFAULT_FILENAME = "unknown filename";

    private Function<FileItem, Photo> convertToPhoto = fileItem ->
            Photo.create(
                    ofNullable(fileItem.getName())
                            .map(FilenameUtils::getName)
                            .orElse(DEFAULT_FILENAME),
                    fileItem.get()
            );

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        photoService.getPhoto(Long.valueOf(request.getParameter("id")))
                .ifPresentOrElse(
                        photoService.throwingConsumerWrapper(
                                photo -> {
                                    response.setContentType("name=" + photo.getName());
                                    response.setContentType("image/png");
                                    response.setHeader(CONTENT_DISPOSITION, "attachment; filename=\"" + photo.getName() + "\"");
                                    response.getOutputStream().write(photo.getImage());
                                }
                        ),
                        () -> sendError(new ErrorResponse(SC_NOT_FOUND, "Photo not found"), response)
                );
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        if (!ServletFileUpload.isMultipartContent(request)) {
            sendError(new ErrorResponse(SC_BAD_REQUEST, "Upload error"), response);
            return;
        }
        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(MEMORY_THRESHOLD);
            factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setFileSizeMax(MAX_FILE_SIZE);
            upload.setSizeMax(MAX_REQUEST_SIZE);
            List<FileItem> formItems = upload.parseRequest(request);
            if (formItems == null || formItems.size() == 0) {
                sendError(new ErrorResponse(SC_BAD_REQUEST, "File content is empty, upload failed."), response);
                return;
            }
            sendResponse(
                    formItems.stream()
                            .filter(not(FileItem::isFormField))
                            .map(convertToPhoto)
                            .map(photoService::savePhoto)
                            .map(PhotoDto::fromEntity)
                            .collect(Collectors.toList()),
                    response
            );
        } catch (Exception e) {
            LOG.error("Upload error", e);
            sendError(new ErrorResponse(SC_NOT_FOUND, "Unexpected error, upload failed."), response);
        }
    }


}
