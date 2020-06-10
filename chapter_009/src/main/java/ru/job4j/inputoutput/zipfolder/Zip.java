package ru.job4j.inputoutput.zipfolder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.inputoutput.scanfilesystem.Search;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zip {

    private static final Logger LOG = LogManager.getLogger(Zip.class);

    public void packFiles(List<File> sources, File target) {
        try (ZipOutputStream zip = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(target)))) {
            for (File file : sources) {
                zip.putNextEntry(new ZipEntry(file.getPath()));
                try (BufferedInputStream out = new BufferedInputStream(new FileInputStream(file))) {
                    zip.write(out.readAllBytes());
                } catch (IOException e) {
                    LOG.error("Error zipping file: {}", file.getAbsolutePath());
                }
            }
        } catch (IOException e) {
            LOG.error("Error writing to: {}", target.getAbsolutePath());
        }
    }

    public void packSingleFile(File source, File target) {
        try (ZipOutputStream zip = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(target)))) {
            zip.putNextEntry(new ZipEntry(source.getPath()));
            try (BufferedInputStream out = new BufferedInputStream(new FileInputStream(source))) {
                zip.write(out.readAllBytes());
            }
        } catch (IOException e) {
            LOG.error("Error writing to: {}", target.getAbsolutePath());
        }
    }

    public static void main(String[] args) throws IOException {
//        new Zip().packSingleFile(
//                new File("./chapter_005/pom.xml"),
//                new File("./chapter_005/pom.zip")
//        );

        ArgZip arguments = new ArgZip(args);
        if (!arguments.isValid()) {
            throw new IllegalArgumentException("Invalid args");
        }
        List<File> files = Search.search(
                Paths.get(arguments.directory()),
                path -> !path.toFile().getName().endsWith(arguments.exclude())
        )
                .stream()
                .peek(p -> LOG.info("Processing path: {}", p.toString()))
                .map(p -> new File(p.toUri()))
                .collect(Collectors.toList());
        LOG.info("Files found: {}", files.size());
        new Zip().packFiles(files, new File(arguments.output()));
    }
}
