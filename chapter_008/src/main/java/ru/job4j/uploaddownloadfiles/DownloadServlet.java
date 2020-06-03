package ru.job4j.uploaddownloadfiles;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DownloadServlet extends HttpServlet {

    private final AttachmentService attachmentService = PhotoService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        resp.setContentType("name=" + name);
        resp.setContentType("image/png");
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
        String filePath = getServletContext().getRealPath("/images/") + File.separator + name;
        attachmentService.getAttachment(filePath)
                .ifPresent(
                        attachmentService.throwingConsumerWrapper(
                                file -> {
                                    try (FileInputStream in = new FileInputStream(file)) {
                                        resp.getOutputStream().write(in.readAllBytes());
                                    }
                                }
                        )
                );
    }

}
